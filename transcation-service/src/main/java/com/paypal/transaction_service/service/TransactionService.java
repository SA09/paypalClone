package com.paypal.transaction_service.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.paypal.transaction_service.dto.MessageDto;
import com.paypal.transaction_service.dto.TransferRequestDto;
import com.paypal.transaction_service.entity.LedgerEntry;
import com.paypal.transaction_service.entity.Transaction;
import com.paypal.transaction_service.repository.LedgerEntryRepository;
import com.paypal.transaction_service.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {
	
    @Autowired
	private TransactionRepository transactionRepository;
    
    @Autowired
	private LedgerEntryRepository ledgerEntryRepository;
    
    @Autowired
	private KafkaProducerService kafkaProducerService;
    
    @Autowired
    private RestTemplate restTemplate;
	

	@Transactional
	public Transaction createTransaction(Long fromUserId, Long toUserId, BigDecimal amount, UUID referenceId) {
		
		Optional<Transaction> transaction = transactionRepository.findByReferenceId(referenceId);
		BigDecimal walletBalance = checkUserBalance(fromUserId);
		System.out.println(walletBalance);
		if(walletBalance.compareTo(amount)<0) {
			 throw new RuntimeException("Insufficient balance");
		}
		if(transaction.isPresent()) {
			return transaction.get();
		}
		
		Transaction tx = new Transaction();
		
		tx.setAmount(amount);
		tx.setStatus("PENDING");
		tx.setCreatedAt(Instant.now());
		tx.setReferenceId(referenceId);
		tx.setFromUserId(fromUserId);
		tx.setToUserId(toUserId);
	    transactionRepository.save(tx);
	    
	    
	    LedgerEntry creditEntry = new LedgerEntry(tx.getId(),toUserId, tx.getAmount());
	    LedgerEntry debitEntry = new LedgerEntry(tx.getId(),fromUserId, tx.getAmount().negate());
	    
	    ledgerEntryRepository.save(debitEntry);
        ledgerEntryRepository.save(creditEntry);
        
        List<LedgerEntry> entiriesTx= ledgerEntryRepository.findByTransactionId(tx.getId());
        BigDecimal sum = BigDecimal.ZERO;
        for(LedgerEntry ledgerEntry : entiriesTx ){
        	sum = sum.add(ledgerEntry.getAmount());
        }
        if(sum.compareTo(BigDecimal.ZERO)!= 0) {
        	tx.setStatus("Rejected");
        	transactionRepository.save(tx);
        	 throw new IllegalStateException("Ledger imbalance detected!");
        }
        transactionRepository.save(tx);
        Map<String, Object> payload = Map.of(
        	    "transactionId", tx.getId(),
        	    "fromUserId", tx.getFromUserId(),
        	    "toUserId", tx.getToUserId(),
        	    "amount", tx.getAmount(),
        	    "status", tx.getStatus()
        	);
      
        
        kafkaProducerService.sendMessage(payload);
        
        return transactionRepository.save(tx);		
	}
	
	private BigDecimal checkUserBalance(Long fromUserId) {
		System.out.println(fromUserId);
		String url = "http://localhost:8083/wallets/get-balance?userId=" + fromUserId;

        try {
            ResponseEntity<BigDecimal> response = restTemplate.getForEntity(url, BigDecimal.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to fetch balance. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while calling wallet service: " + e.getMessage(), e);
        }
    }
	

}

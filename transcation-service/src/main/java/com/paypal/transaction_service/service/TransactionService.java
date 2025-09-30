package com.paypal.transaction_service.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.paypal.transaction_service.dto.MessageDto;
import com.paypal.transaction_service.dto.TransferRequestDto;
import com.paypal.transaction_service.entity.LedgerEntry;
import com.paypal.transaction_service.entity.Transaction;
import com.paypal.transaction_service.repository.LedgerEntryRepository;
import com.paypal.transaction_service.repository.TransactionRepository;

@Service
public class TransactionService {
	
    @Autowired
	private TransactionRepository transactionRepository;
    
    @Autowired
	private LedgerEntryRepository ledgerEntryRepository;
    
    @Autowired
	private KafkaProducerService kafkaProducerService;
	
	
	public Transaction createTransaction(Long fromUserId, Long toUserId, TransferRequestDto transferRequest, UUID referenceId) {
		
		Optional<Transaction> transaction = transactionRepository.findByReferenceId(transferRequest.getReferanceNumber());
		
		if(transaction.isPresent()) {
			return transaction.get();
		}
		
		Transaction tx = new Transaction();
		
		tx.setAmount(transferRequest.getAmount());
		tx.setStatus("PENDING");
		tx.setCreatedAt(Instant.now());
		tx.setReferenceId(referenceId);
		tx.setFromUserId(fromUserId);
		tx.setToUserId(toUserId);
	    transactionRepository.save(tx);
	    
	    
	    LedgerEntry creditEntry = new LedgerEntry(tx.getId(),toUserId, transferRequest.getAmount());
	    LedgerEntry debitEntry = new LedgerEntry(tx.getId(),fromUserId, transferRequest.getAmount().negate());
	    
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
        MessageDto message = new MessageDto();
        message.setId(UUID.randomUUID().toString());
        message.setTimestamp(System.currentTimeMillis());
        
        kafkaProducerService.sendMessage(message);
        
        return transactionRepository.save(tx);
		
		
		
	}

}

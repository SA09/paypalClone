package com.paypal.transaction_service.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private LedgerEntryRepository ledgerEntryRepository
	;
	
	public Transaction createTransaction(Long fromUserId, Long toUserId, UUID referanceNumber, BigDecimal amount ) {
		
		Optional<Transaction> transaction = transactionRepository.findByReferenceId(referanceNumber);
		
		if(transaction.isPresent()) {
			return transaction.get();
		}
		
		Transaction tx = new Transaction();
		
		tx.setAmount(amount);
		tx.setStatus("PENDING");
		tx.setCreatedAt(Instant.now());
		tx.setReferenceId(referanceNumber);
		tx.setFromUserId(fromUserId);
		tx.setToUserId(toUserId);
	    transactionRepository.save(tx);
	    
	    
	    LedgerEntry creditEntry = new LedgerEntry(tx.getId(),toUserId, amount);
	    LedgerEntry debitEntry = new LedgerEntry(tx.getId(),fromUserId, amount.negate());
	    
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
        
        
        tx.setStatus("SUCCESS");
        return transactionRepository.save(tx);
		
		
		
	}

}

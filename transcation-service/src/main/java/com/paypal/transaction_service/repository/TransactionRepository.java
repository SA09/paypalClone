package com.paypal.transaction_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paypal.transaction_service.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
        
	Optional<Transaction> findByReferenceId(UUID referenceId);
	
}

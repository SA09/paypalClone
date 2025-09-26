package com.paypal.transaction_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paypal.transaction_service.entity.LedgerEntry;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long>{

	List<LedgerEntry> findByTransactionId(Long transactionId);

}

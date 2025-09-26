package com.paypal.transaction_service.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LedgerEntry {
     
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long transactionId;
	
	private Long userId;
	
	private String type;
	
	@Column(nullable = false)
    private BigDecimal amount;
	
	@Column(nullable = false, updatable = false)
    private Instant createdAt;
	
	@PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

	public LedgerEntry(Long transactionId, Long userId, BigDecimal amount){
		this.transactionId = transactionId;
		this.userId = userId;
		this.amount = amount;
	}


	
	
	
	
}

package com.paypal.transaction_service.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transaction {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID referenceId;

    @Column(nullable = false)
    private Long fromUserId;

    @Column(nullable = false)
    private Long toUserId;

    @Column(nullable = false, precision = 19, scale = 2) // good for money
    private BigDecimal amount;

    @Column(nullable = false)
    private String status; // PENDING, SUCCESS, FAILED

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        if (referenceId == null) {
            referenceId = UUID.randomUUID();
        }
        if (status == null) {
            status = "PENDING";
        }
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}


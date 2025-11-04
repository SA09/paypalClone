package com.paypal.wallet_service.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WalletHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "walletId")
    private UserWallet wallet;
   
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String status = "ACTIVE";
    
    private Instant createAt = Instant.now();
    
    private Instant expiresAt;

	public WalletHold(UserWallet wallet, BigDecimal amount, String status, Instant expiresAt) {
		this.wallet = wallet;
		this.amount = amount;
		this.status = status;
		this.expiresAt = expiresAt;
	}
    
    
    
}

package com.paypal.wallet_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Entity
@Data
public class UserWallet {
	
	@Id
	private Long id;  // user ID from the user service 
	
	private BigDecimal WalletAmount;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public UserWallet() {
	}
	
	public UserWallet(Long id, BigDecimal walletAmount, LocalDateTime createdAt) {
		this.id = id;
		this.WalletAmount = walletAmount;
		this.createdAt = createdAt;
	}
	
	@PrePersist
	private void createdAt(){
		if(this.createdAt != null) {
			this.createdAt = LocalDateTime.now();
			this.updatedAt =  LocalDateTime.now();
		}
	}
	
	@PreUpdate
	private void updateAt() {
		this.updatedAt =  LocalDateTime.now();
	}

	
	
	

}

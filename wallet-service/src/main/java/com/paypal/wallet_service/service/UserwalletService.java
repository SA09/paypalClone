package com.paypal.wallet_service.service;



import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.wallet_service.entity.UserWallet;
import com.paypal.wallet_service.repository.UserWalletRepository;

@Service
public class UserwalletService {
	
	@Autowired
	private UserWalletRepository userWalletRepository;
	
	public UserWallet createWallet(Long userId, String userName){
		UserWallet newWallet = new UserWallet(userId, BigDecimal.ZERO, LocalDateTime.now(),userName);
		userWalletRepository.save(newWallet);
		return newWallet;
	}
	
	public BigDecimal getBalance(Long id){
	    UserWallet wallet = userWalletRepository.findById(id).orElseThrow(()-> new RuntimeException("This user does not exist"));
	    BigDecimal amount = wallet.getWalletAmount();
	    return amount;
	}
	
	
   public BigDecimal creditBalance(Long userId, BigDecimal amount) {
	   UserWallet wallet = userWalletRepository.findById(userId).orElseThrow(()-> new RuntimeException("This user does not exist"));
	   BigDecimal totalAmount = wallet.getWalletAmount().add(amount);
	   wallet.setWalletAmount(totalAmount);
	   userWalletRepository.save(wallet);
	   return totalAmount;
	   
   }
   
   public BigDecimal debitBalance(Long userId, BigDecimal amount) {
	   UserWallet wallet = userWalletRepository.findById(userId).orElseThrow(()-> new RuntimeException("This user does not exist"));
	   BigDecimal totalAmount = wallet.getWalletAmount().subtract(amount);
	   wallet.setWalletAmount(totalAmount);
	   userWalletRepository.save(wallet);
	   return totalAmount;
   }
	
	

}

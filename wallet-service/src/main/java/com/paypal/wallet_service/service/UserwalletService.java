package com.paypal.wallet_service.service;



import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paypal.wallet_service.entity.UserWallet;
import com.paypal.wallet_service.entity.WalletHold;
import com.paypal.wallet_service.repository.UserWalletRepository;
import com.paypal.wallet_service.repository.WalletHoldRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserwalletService {
	
	@Autowired
	private UserWalletRepository userWalletRepository;
	
	@Autowired
	private WalletHoldRepository walletHoldRepository;
	
	@Value("$time.hold_wait")
	private int expiresIn;
	
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
	   BigDecimal avilableAmount = wallet.getAvilableAmount().add(amount);
	   wallet.setWalletAmount(totalAmount);
	   wallet.setAvilableAmount(avilableAmount);
	   userWalletRepository.save(wallet);
	   return totalAmount;
	   
   }
   
   public BigDecimal debitBalance(Long userId, BigDecimal amount) {
	   UserWallet wallet = userWalletRepository.findById(userId).orElseThrow(()-> new RuntimeException("This user does not exist"));
	   if (amount.compareTo(wallet.getAvilableAmount())>0) {
           throw new RuntimeException("Not enough balance");
       }
	   BigDecimal totalAmount = wallet.getWalletAmount().subtract(amount);
	   BigDecimal avilableAmount = wallet.getAvilableAmount().subtract(amount);
	   wallet.setWalletAmount(totalAmount);
	   wallet.setAvilableAmount(avilableAmount);
	   userWalletRepository.save(wallet);
	   return totalAmount;
   }
   
   public void placeHold(Long userId, BigDecimal holdAmount) {
	 UserWallet wallet = userWalletRepository.findById(userId).orElseThrow(()-> new RuntimeException("This user doesnot exist"));
	 BigDecimal avilableAmount = wallet.getAvilableAmount();
	 if(holdAmount.compareTo(avilableAmount)>0) {
		 throw new RuntimeException("Insuffcient Balance");
	 }
	 Instant expiresAt = Instant.now().plusSeconds(expiresIn);
	 new WalletHold(wallet, avilableAmount, "ACTIVE", expiresAt);
	 wallet.getAvilableAmount().subtract(holdAmount); 
	 userWalletRepository.save(wallet);
	 
   }
   public void capturehold(Long walletHoldId){
	   WalletHold walletHold = walletHoldRepository.findById(walletHoldId).orElseThrow(() -> new RuntimeException("This hold does't exist"));
	   UserWallet wallet = walletHold.getWallet();
	   BigDecimal holdAmount =  walletHold.getAmount();

	   wallet.setWalletAmount(wallet.getWalletAmount().subtract(holdAmount));
	
	   walletHold.setStatus("CAPTURED");
	   walletHoldRepository.save(walletHold);
	   
   }
   
   public void releaseHold(Long walletHoldId){
	   WalletHold walletHold = walletHoldRepository.findById(walletHoldId).orElseThrow(() -> new RuntimeException("This hold does't exist"));
	   UserWallet wallet = walletHold.getWallet();
	   BigDecimal holdAmount =  walletHold.getAmount();

	   wallet.setAvilableAmount(wallet.getAvilableAmount().add(holdAmount));
	
	   walletHold.setStatus("RELEASED");
	   walletHoldRepository.save(walletHold);
	   
   }
	
	

}

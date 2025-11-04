package com.paypal.wallet_service.scheduler;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.paypal.wallet_service.entity.WalletHold;
import com.paypal.wallet_service.repository.WalletHoldRepository;
import com.paypal.wallet_service.service.UserwalletService;

@Component
public class WalletHoldScheduler {
	
	@Autowired
	private UserwalletService userwalletService;
	
	@Autowired
	private WalletHoldRepository walletHoldRepository;
	
	@Scheduled
	public void walletHoldScheduler() {
		List<WalletHold> expiredWallet = walletHoldRepository.findByStatusAndExpiresAt("ACTIVE", Instant.now());
		for(WalletHold walletHold : expiredWallet) {
			userwalletService.releaseHold(walletHold.getId());
		}
	}
}

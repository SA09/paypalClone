package com.paypal.wallet_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.wallet_service.service.UserwalletService;

@RestController
@RequestMapping("/wallets")
public class WalletController {
	
	@Autowired
	private UserwalletService userwalletService;
	
	@GetMapping("/get-balance")
	public ResponseEntity<?> getBalance(@RequestParam Long userId) {
		try {
			return ResponseEntity.ok(userwalletService.getBalance(userId));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	

	
	

}

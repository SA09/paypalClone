package com.paypal.wallet_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.wallet_service.entity.UserWallet;
import com.paypal.wallet_service.service.UserwalletService;

@RestController
@RequestMapping("/api")
public class WalletController {
	
	@Autowired
	private UserwalletService userwalletService;
	
	
	

}

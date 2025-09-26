package com.paypal.transaction_service.controller;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.transaction_service.dto.TransferRequestDto;
import com.paypal.transaction_service.service.TransactionService;

@RestController
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService; 
	
	@PostMapping
	public ResponseEntity<> addTransferRequest(@RequestBody TransferRequestDto request, @AuthenticationPrincipal AuthenticatedPrincipal principle ) {
		try {
			Long fromUserId = Long.parseLong(principle.getName());
			
			UUID referenceId = request.getReferanceNumber() != null
	                ? request.getReferanceNumber()
	                : UUID.randomUUID();
			
			transactionService.createTransaction(fromUserId, request.getToUserId(), request);
		}
		catch{
			
		}
	}

}

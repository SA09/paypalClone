package com.paypal.transaction_service.controller;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.transaction_service.dto.TransferRequestDto;
import com.paypal.transaction_service.entity.Transaction;
import com.paypal.transaction_service.service.TransactionService;

@RestController
@RequestMapping("/api")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService; 
	
	@PostMapping("/transfer")

	public ResponseEntity<?> addTransferRequest(@RequestBody TransferRequestDto request) {

	    try {
	        UUID referenceId = request.getReferanceNumber() != null   //Should be generate from the frontend 
	        		
	                ? request.getReferanceNumber()
	                : UUID.randomUUID();

	        transactionService.createTransaction(request.getFromUserId(), request.getToUserId(), request,referenceId);

	        return ResponseEntity.ok("Transaction created successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}


}

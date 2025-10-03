package com.paypal.wallet_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.wallet_service.dto.TransactionEventDto;

import jakarta.transaction.Transactional;

@Service
public class KafkaConsumerService {
	
	@Autowired
	private UserwalletService userwalletService;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@KafkaListener(topics = "transaction", groupId = "wallet-service")
	@Transactional
	public void updateBalance(String message) throws JsonMappingException, JsonProcessingException {
         TransactionEventDto event = objectMapper.readValue(message, TransactionEventDto.class);

         Long fromUserId = event.getFromUserId();
         Long toUserId = event.getToUserId();
         
         userwalletService.creditBalance(toUserId,event.getAmount());
		 userwalletService.debitBalance(fromUserId,event.getAmount());
	}

}

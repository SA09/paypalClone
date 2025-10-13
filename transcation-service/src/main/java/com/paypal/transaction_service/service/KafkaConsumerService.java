package com.paypal.transaction_service.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.transaction_service.dto.TransactionEventDto;
import com.paypal.transaction_service.entity.Transaction;
import com.paypal.transaction_service.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class KafkaConsumerService {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@KafkaListener(topics="trasaction_update_status",groupId = "transaction-update-service")
	public void updateTrasactionStatus(String message) throws JsonMappingException, JsonProcessingException {
		System.out.println("Hello, kafka listerner");
		TransactionEventDto event = objectMapper.readValue(message, TransactionEventDto.class);
		System.out.println(event.getStatus());
		if("SUCCESS".equals(event.getStatus())) {
			Transaction transaction =  transactionRepository.findById(event.getTransactionId()).orElseThrow(()-> new RuntimeException(" This transaction Does not Exist"));
			transaction.setStatus("SUCCESS");
			transactionRepository.save(transaction);
	        log.info("Transaction {} updated to SUCCESS", event.getTransactionId());

		}		
		
	}

}

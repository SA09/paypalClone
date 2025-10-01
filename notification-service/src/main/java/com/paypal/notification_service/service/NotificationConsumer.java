package com.paypal.notification_service.service;

import java.math.BigDecimal;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.notification_service.dto.TransactionEventDto;
import com.paypal.notification_service.entity.Notification;
import com.paypal.notification_service.repository.NotificationRepository;


@Service
public class NotificationConsumer {
	
	private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
	
	@Autowired
	private NotificationRepository notificationRepository;
    
	@KafkaListener(topics = "transaction", groupId = "notification-service")
    public void consumeTransactionEvent(String message) throws JsonMappingException, JsonProcessingException {
		 ObjectMapper objectMapper = new ObjectMapper();
         TransactionEventDto event = objectMapper.readValue(message, TransactionEventDto.class);

         Long userId = event.getFromUserId();
         BigDecimal amount = new BigDecimal(event.getAmount().toString());
         String referenceId = event.getTransactionId().toString();

         String msg = "Your payment of ₹" + amount + " has been initiated. Reference: " + referenceId;
         System.out.println("MSG"+msg);

         // save notification in DB
         Notification notification = new Notification();
         notification.setUserId(userId);
         notification.setType("EMAIL");
         notification.setMessage(msg);
         notification.setStatus("PENDING");
         notification = notificationRepository.save(notification);



         notification.setStatus("SENT");
         notification.setSentAt(Instant.now());
         notificationRepository.save(notification);

         log.info("Notification sent for transaction {}", referenceId);
    }
}

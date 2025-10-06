package com.paypal.user_service.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class UserProducerService {
    
	private static final Logger log = LoggerFactory.getLogger(UserProducerService.class);
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    
    @Value("${kafka.topic.name}")
    private String topicName;

    public void sendMessage(Map<String, Object> payload) {
    	System.out.println("hello");
    	System.out.println(payload.get("userId"));
        log.info("Sending message: {}", payload);
        String transactionId = payload.get("userId").toString();
       
        Message<Map<String, Object>> msg = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.KEY, transactionId)
                .build();
        
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(msg);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully: offset={}", 
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send message", ex);
            }
        });
    }
}

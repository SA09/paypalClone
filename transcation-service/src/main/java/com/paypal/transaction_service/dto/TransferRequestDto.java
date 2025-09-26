package com.paypal.transaction_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransferRequestDto {
	
	private Long fromUserId;
	
	private Long toUserId;
	
	private BigDecimal amount;
	
	private UUID referanceNumber;
	
	

}

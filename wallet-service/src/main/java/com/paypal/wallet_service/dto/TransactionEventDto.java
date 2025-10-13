package com.paypal.wallet_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEventDto {

	    private Long transactionId;   // referenceId from transaction service
	    private Long fromUserId;
	    private Long toUserId;
	    private BigDecimal amount;
	    private String status;          
	

}

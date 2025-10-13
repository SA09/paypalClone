package com.paypal.transaction_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionEventDto {

	    private Long transactionId;   // referenceId from transaction service
	    private Long fromUserId;
	    private Long toUserId;
	    private BigDecimal amount;
	    private String status;          
	

}

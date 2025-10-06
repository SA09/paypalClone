package com.paypal.wallet_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class UserDto {

	private Long userId;
	private String userName;
	private String email;

}

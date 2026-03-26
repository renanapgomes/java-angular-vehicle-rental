package com.rental.solides.dto.response;

import lombok.Data;

@Data
public class ClientResponseDto {
	private String id;
	private String fullName;
	private String document;
	private String email;
	private String phone;
}

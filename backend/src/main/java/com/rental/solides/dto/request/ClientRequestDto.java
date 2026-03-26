package com.rental.solides.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientRequestDto {

	@NotBlank
	private String fullName;

	@NotBlank
	private String document;

	@NotBlank
	@Email
	private String email;

	private String phone;
}

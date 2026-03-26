package com.rental.solides.infrastructure.web;

import com.rental.solides.domain.Client;
import com.rental.solides.dto.response.ClientResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ClientWebMapper {

	public ClientResponseDto toResponseDto(Client c) {
		ClientResponseDto dto = new ClientResponseDto();
		dto.setId(c.getId() != null ? c.getId().toString() : null);
		dto.setFullName(c.getFullName());
		dto.setDocument(c.getDocument());
		dto.setEmail(c.getEmail());
		dto.setPhone(c.getPhone());
		return dto;
	}
}

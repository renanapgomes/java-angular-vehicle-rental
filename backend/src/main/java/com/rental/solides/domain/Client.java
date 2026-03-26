package com.rental.solides.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Client {

	private UUID id;
	private final String fullName;
	private final String document;
	private final String email;
	private final String phone;

	public static Client create(String fullName, String document, String email, String phone) {
		if (fullName == null || fullName.isBlank()) {
			throw new BusinessException("O nome completo é obrigatório");
		}
		if (document == null || document.isBlank()) {
			throw new BusinessException("O documento (CPF) é obrigatório");
		}
		String doc = document.replaceAll("\\D", "");
		if (doc.length() != 11) {
			throw new BusinessException("CPF deve conter 11 dígitos");
		}
		if (email == null || email.isBlank()) {
			throw new BusinessException("O e-mail é obrigatório");
		}
		if (!email.contains("@")) {
			throw new BusinessException("E-mail inválido");
		}
		return new Client(null, fullName.trim(), doc, email.trim(), phone != null ? phone.trim() : null);
	}

	public static Client reconstitute(UUID id, String fullName, String document, String email, String phone) {
		return new Client(id, fullName, document, email, phone);
	}

	public void setId(UUID id) {
		this.id = id;
	}
}

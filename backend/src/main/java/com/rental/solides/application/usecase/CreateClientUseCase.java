package com.rental.solides.application.usecase;

import com.rental.solides.application.port.SaveClientPort;
import com.rental.solides.domain.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateClientUseCase {

	private final SaveClientPort saveClientPort;

	public Client execute(String fullName, String document, String email, String phone) {
		Client client = Client.create(fullName, document, email, phone);
		return saveClientPort.save(client);
	}
}

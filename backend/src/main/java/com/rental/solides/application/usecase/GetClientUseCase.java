package com.rental.solides.application.usecase;

import com.rental.solides.application.exception.ResourceNotFoundException;
import com.rental.solides.application.port.LoadClientPort;
import com.rental.solides.domain.Client;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetClientUseCase {

	private final LoadClientPort loadClientPort;

	public Client execute(UUID id) {
		return loadClientPort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id));
	}
}

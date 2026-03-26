package com.rental.solides.application.usecase;

import com.rental.solides.application.port.LoadClientPort;
import com.rental.solides.domain.Client;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListClientsUseCase {

	private final LoadClientPort loadClientPort;

	public List<Client> execute() {
		return loadClientPort.findAll();
	}
}

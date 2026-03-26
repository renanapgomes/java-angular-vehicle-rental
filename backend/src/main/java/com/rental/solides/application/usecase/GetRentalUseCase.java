package com.rental.solides.application.usecase;

import com.rental.solides.application.exception.ResourceNotFoundException;
import com.rental.solides.application.port.LoadRentalPort;
import com.rental.solides.domain.Rental;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetRentalUseCase {

	private final LoadRentalPort loadRentalPort;

	public Rental execute(UUID id) {
		return loadRentalPort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Aluguel não encontrado: " + id));
	}
}

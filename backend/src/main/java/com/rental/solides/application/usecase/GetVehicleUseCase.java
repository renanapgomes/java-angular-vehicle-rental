package com.rental.solides.application.usecase;

import com.rental.solides.application.exception.ResourceNotFoundException;
import com.rental.solides.application.port.LoadVehiclePort;
import com.rental.solides.domain.Vehicle;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetVehicleUseCase {

	private final LoadVehiclePort loadVehiclePort;

	public Vehicle execute(UUID id) {
		return loadVehiclePort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado: " + id));
	}
}

package com.rental.solides.application.usecase;

import com.rental.solides.application.port.LoadVehiclePort;
import com.rental.solides.domain.Vehicle;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListVehiclesUseCase {

	private final LoadVehiclePort loadVehiclePort;

	public List<Vehicle> execute() {
		return loadVehiclePort.findAll();
	}
}

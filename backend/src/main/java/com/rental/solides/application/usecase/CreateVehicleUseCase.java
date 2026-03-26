package com.rental.solides.application.usecase;

import com.rental.solides.application.port.SaveVehiclePort;
import com.rental.solides.domain.Vehicle;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CreateVehicleUseCase {

	private final SaveVehiclePort saveVehiclePort;

	public Vehicle execute(String plate, String brand, String model, Integer year, BigDecimal dailyRate) {
		Vehicle vehicle = Vehicle.create(plate, brand, model, year, dailyRate);
		return saveVehiclePort.save(vehicle);
	}
}

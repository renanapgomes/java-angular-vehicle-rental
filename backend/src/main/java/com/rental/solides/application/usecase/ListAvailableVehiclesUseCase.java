package com.rental.solides.application.usecase;

import com.rental.solides.application.port.AvailableVehiclesQueryPort;
import com.rental.solides.domain.Vehicle;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ListAvailableVehiclesUseCase {

	private final AvailableVehiclesQueryPort availableVehiclesQueryPort;

	public List<Vehicle> execute(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			throw new IllegalArgumentException("Datas inicial e final são obrigatórias");
		}
		return availableVehiclesQueryPort.findAvailable(start, end);
	}
}

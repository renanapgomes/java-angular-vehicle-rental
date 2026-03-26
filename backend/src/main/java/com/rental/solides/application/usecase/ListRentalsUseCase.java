package com.rental.solides.application.usecase;

import com.rental.solides.application.port.LoadRentalPort;
import com.rental.solides.domain.Rental;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListRentalsUseCase {

	private final LoadRentalPort loadRentalPort;

	public List<Rental> execute() {
		return loadRentalPort.findAllOrderByStartDesc();
	}
}

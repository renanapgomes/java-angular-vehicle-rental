package com.rental.solides.application.port;

import com.rental.solides.domain.Vehicle;

import java.time.LocalDate;
import java.util.List;

public interface AvailableVehiclesQueryPort {
	List<Vehicle> findAvailable(LocalDate start, LocalDate end);
}

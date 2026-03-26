package com.rental.solides.application.port;

import com.rental.solides.domain.Vehicle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadVehiclePort {
	Optional<Vehicle> findById(UUID id);

	List<Vehicle> findAll();
}

package com.rental.solides.application.port;

import com.rental.solides.domain.Vehicle;

public interface SaveVehiclePort {
	Vehicle save(Vehicle vehicle);
}

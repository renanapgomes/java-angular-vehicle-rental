package com.rental.solides.application.port;

import java.util.UUID;

public interface RentalDeletionPolicyPort {
	boolean existsActiveByVehicleId(UUID vehicleId);

	boolean existsActiveByClientId(UUID clientId);

	void deleteCancelledByVehicleId(UUID vehicleId);

	void deleteCancelledByClientId(UUID clientId);
}


package com.rental.solides.infrastructure.cache;

import com.rental.solides.application.port.AvailableVehiclesQueryPort;
import com.rental.solides.domain.Vehicle;
import com.rental.solides.infrastructure.persistence.VehiclePersistenceAdapter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CachedAvailableVehiclesQuery implements AvailableVehiclesQueryPort {

	private final VehiclePersistenceAdapter vehiclePersistenceAdapter;

	public CachedAvailableVehiclesQuery(VehiclePersistenceAdapter vehiclePersistenceAdapter) {
		this.vehiclePersistenceAdapter = vehiclePersistenceAdapter;
	}

	@Override
	@Cacheable(cacheNames = "availableVehicles", key = "#start.toString() + '_' + #end.toString()")
	public List<Vehicle> findAvailable(LocalDate start, LocalDate end) {
		return vehiclePersistenceAdapter.findAvailableInPeriod(start, end);
	}
}

package com.rental.solides.infrastructure.persistence;

import com.rental.solides.application.port.LoadVehiclePort;
import com.rental.solides.application.port.SaveVehiclePort;
import com.rental.solides.application.port.DeleteVehiclePort;
import com.rental.solides.domain.RentalStatus;
import com.rental.solides.domain.Vehicle;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class VehiclePersistenceAdapter implements SaveVehiclePort, LoadVehiclePort, DeleteVehiclePort {

	private final VehicleRepository vehicleRepository;

	public VehiclePersistenceAdapter(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public Vehicle save(Vehicle vehicle) {
		VehicleEntity entity = toEntity(vehicle);
		VehicleEntity saved = vehicleRepository.save(entity);
		return toDomain(saved);
	}

	@Override
	public Optional<Vehicle> findById(UUID id) {
		return vehicleRepository.findById(id).map(this::toDomain);
	}

	@Override
	public List<Vehicle> findAll() {
		return vehicleRepository.findAll().stream().map(this::toDomain).toList();
	}

	@Override
	public void deleteById(UUID id) {
		vehicleRepository.deleteById(id);
	}

	public List<Vehicle> findAvailableInPeriod(LocalDate start, LocalDate end) {
		return vehicleRepository.findAvailableInPeriod(start, end, RentalStatus.ACTIVE).stream()
				.map(this::toDomain)
				.toList();
	}

	private VehicleEntity toEntity(Vehicle v) {
		VehicleEntity e = new VehicleEntity();
		e.setId(v.getId());
		e.setPlate(v.getPlate());
		e.setBrand(v.getBrand());
		e.setModel(v.getModel());
		e.setYear(v.getYear());
		e.setDailyRate(v.getDailyRate());
		return e;
	}

	private Vehicle toDomain(VehicleEntity e) {
		return Vehicle.reconstitute(e.getId(), e.getPlate(), e.getBrand(), e.getModel(), e.getYear(),
				e.getDailyRate());
	}
}

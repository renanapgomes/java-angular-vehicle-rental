package com.rental.solides.infrastructure.persistence;

import com.rental.solides.application.port.LoadRentalPort;
import com.rental.solides.application.port.RentalDeletionPolicyPort;
import com.rental.solides.application.port.SaveRentalPort;
import com.rental.solides.domain.RentalStatus;
import com.rental.solides.domain.Rental;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RentalPersistenceAdapter implements SaveRentalPort, LoadRentalPort, RentalDeletionPolicyPort {

	private final RentalRepository rentalRepository;
	private final VehicleRepository vehicleRepository;
	private final ClientRepository clientRepository;

	public RentalPersistenceAdapter(RentalRepository rentalRepository, VehicleRepository vehicleRepository,
			ClientRepository clientRepository) {
		this.rentalRepository = rentalRepository;
		this.vehicleRepository = vehicleRepository;
		this.clientRepository = clientRepository;
	}

	@Override
	public Rental save(Rental rental) {
		VehicleEntity vehicle = vehicleRepository.findById(rental.getVehicleId())
				.orElseThrow(() -> new IllegalStateException("Veículo não encontrado na persistência"));
		ClientEntity client = clientRepository.findById(rental.getClientId())
				.orElseThrow(() -> new IllegalStateException("Cliente não encontrado na persistência"));
		RentalEntity entity = toEntity(rental, vehicle, client);
		RentalEntity saved = rentalRepository.save(entity);
		return toDomain(saved);
	}

	@Override
	public Optional<Rental> findById(UUID id) {
		return rentalRepository.findById(id).map(this::toDomain);
	}

	@Override
	public List<Rental> findAllOrderByStartDesc() {
		return rentalRepository.findAllByOrderByStartDateDesc().stream().map(this::toDomain).toList();
	}

	@Override
	public boolean existsActiveByVehicleId(UUID vehicleId) {
		return rentalRepository.existsByVehicle_IdAndStatus(vehicleId, RentalStatus.ACTIVE);
	}

	@Override
	public boolean existsActiveByClientId(UUID clientId) {
		return rentalRepository.existsByClient_IdAndStatus(clientId, RentalStatus.ACTIVE);
	}

	@Override
	public void deleteCancelledByVehicleId(UUID vehicleId) {
		rentalRepository.deleteByVehicle_IdAndStatus(vehicleId, RentalStatus.CANCELLED);
	}

	@Override
	public void deleteCancelledByClientId(UUID clientId) {
		rentalRepository.deleteByClient_IdAndStatus(clientId, RentalStatus.CANCELLED);
	}

	private RentalEntity toEntity(Rental r, VehicleEntity vehicle, ClientEntity client) {
		RentalEntity e = new RentalEntity();
		e.setId(r.getId());
		e.setVehicle(vehicle);
		e.setClient(client);
		e.setStartDate(r.getStartDate());
		e.setEndDate(r.getEndDate());
		e.setTotalAmount(r.getTotalAmount());
		e.setStatus(r.getStatus());
		return e;
	}

	private Rental toDomain(RentalEntity e) {
		return Rental.reconstitute(e.getId(), e.getVehicle().getId(), e.getClient().getId(), e.getStartDate(),
				e.getEndDate(), e.getTotalAmount(), e.getStatus());
	}
}

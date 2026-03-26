package com.rental.solides.infrastructure.persistence;

import com.rental.solides.application.port.RentalOverlapPort;
import com.rental.solides.domain.RentalStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class RentalOverlapAdapter implements RentalOverlapPort {

	private final RentalRepository rentalRepository;

	public RentalOverlapAdapter(RentalRepository rentalRepository) {
		this.rentalRepository = rentalRepository;
	}

	@Override
	public boolean existsActiveOverlap(UUID vehicleId, LocalDate start, LocalDate end, UUID excludeRentalId) {
		return rentalRepository.existsActiveOverlap(vehicleId, start, end, excludeRentalId, RentalStatus.ACTIVE);
	}
}

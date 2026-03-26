package com.rental.solides.application.port;

import com.rental.solides.domain.Rental;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadRentalPort {
	Optional<Rental> findById(UUID id);

	List<Rental> findAllOrderByStartDesc();
}

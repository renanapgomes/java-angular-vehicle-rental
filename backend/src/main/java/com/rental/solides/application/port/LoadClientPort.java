package com.rental.solides.application.port;

import com.rental.solides.domain.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadClientPort {
	Optional<Client> findById(UUID id);

	List<Client> findAll();
}

package com.rental.solides.application.port;

import java.time.LocalDate;
import java.util.UUID;

public interface RentalOverlapPort {

	/**
	 * Verifica se existe aluguel ATIVO para o veículo que intersecta [start, end] (inclusivo).
	 */
	boolean existsActiveOverlap(UUID vehicleId, LocalDate start, LocalDate end, UUID excludeRentalId);
}

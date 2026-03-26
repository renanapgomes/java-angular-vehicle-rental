package com.rental.solides.application.usecase;

import com.rental.solides.application.exception.ResourceNotFoundException;
import com.rental.solides.application.port.LoadRentalPort;
import com.rental.solides.application.port.RentalMutationNotifier;
import com.rental.solides.application.port.SaveRentalPort;
import com.rental.solides.domain.Rental;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CancelRentalUseCase {

	private final LoadRentalPort loadRentalPort;
	private final SaveRentalPort saveRentalPort;
	private final RentalMutationNotifier rentalMutationNotifier;

	public Rental execute(UUID id) {
		Rental rental = loadRentalPort.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Aluguel não encontrado: " + id));
		rental.cancel();
		Rental saved = saveRentalPort.save(rental);
		rentalMutationNotifier.onRentalChanged();
		return saved;
	}
}

package com.rental.solides.application.usecase;

import com.rental.solides.application.exception.ResourceNotFoundException;
import com.rental.solides.application.port.LoadClientPort;
import com.rental.solides.application.port.LoadVehiclePort;
import com.rental.solides.application.port.RentalMutationNotifier;
import com.rental.solides.application.port.RentalOverlapPort;
import com.rental.solides.application.port.SaveRentalPort;
import com.rental.solides.domain.BusinessException;
import com.rental.solides.domain.Client;
import com.rental.solides.domain.Rental;
import com.rental.solides.domain.Vehicle;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateRentalUseCase {

	private final LoadVehiclePort loadVehiclePort;
	private final LoadClientPort loadClientPort;
	private final RentalOverlapPort rentalOverlapPort;
	private final SaveRentalPort saveRentalPort;
	private final RentalMutationNotifier rentalMutationNotifier;

	public Rental execute(UUID vehicleId, UUID clientId, LocalDate startDate, LocalDate endDate) {
		Vehicle vehicle = loadVehiclePort.findById(vehicleId)
				.orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado: " + vehicleId));
		Client client = loadClientPort.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + clientId));

		if (rentalOverlapPort.existsActiveOverlap(vehicleId, startDate, endDate, null)) {
			throw new BusinessException(
					"Veículo indisponível no período informado: já existe aluguel ativo com datas conflitantes.");
		}

		Rental rental = Rental.create(vehicleId, client.getId(), startDate, endDate, vehicle.getDailyRate());
		Rental saved = saveRentalPort.save(rental);
		rentalMutationNotifier.onRentalChanged();
		return saved;
	}
}

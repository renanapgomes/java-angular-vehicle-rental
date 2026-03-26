package com.rental.solides.application.usecase;

import com.rental.solides.application.exception.ResourceNotFoundException;
import com.rental.solides.application.port.DeleteVehiclePort;
import com.rental.solides.application.port.LoadVehiclePort;
import com.rental.solides.application.port.RentalDeletionPolicyPort;
import com.rental.solides.application.port.RentalMutationNotifier;
import com.rental.solides.domain.BusinessException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteVehicleUseCase {

	private final LoadVehiclePort loadVehiclePort;
	private final DeleteVehiclePort deleteVehiclePort;
	private final RentalDeletionPolicyPort rentalDeletionPolicyPort;
	private final RentalMutationNotifier rentalMutationNotifier;

	public void execute(UUID vehicleId) {
		loadVehiclePort.findById(vehicleId)
				.orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado: " + vehicleId));

		if (rentalDeletionPolicyPort.existsActiveByVehicleId(vehicleId)) {
			throw new BusinessException("Não é possível excluir veículo com aluguel ativo.");
		}

		// Regra: histórico cancelado pode ser removido para permitir exclusão do veículo.
		rentalDeletionPolicyPort.deleteCancelledByVehicleId(vehicleId);
		deleteVehiclePort.deleteById(vehicleId);
		rentalMutationNotifier.onRentalChanged();
	}
}


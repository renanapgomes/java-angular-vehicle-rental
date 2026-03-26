package com.rental.solides.application.usecase;

import com.rental.solides.application.exception.ResourceNotFoundException;
import com.rental.solides.application.port.DeleteClientPort;
import com.rental.solides.application.port.LoadClientPort;
import com.rental.solides.application.port.RentalDeletionPolicyPort;
import com.rental.solides.domain.BusinessException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteClientUseCase {

	private final LoadClientPort loadClientPort;
	private final DeleteClientPort deleteClientPort;
	private final RentalDeletionPolicyPort rentalDeletionPolicyPort;

	public void execute(UUID clientId) {
		loadClientPort.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + clientId));

		if (rentalDeletionPolicyPort.existsActiveByClientId(clientId)) {
			throw new BusinessException("Não é possível excluir cliente com aluguel ativo.");
		}

		// Regra: histórico cancelado pode ser removido para permitir exclusão do cliente.
		rentalDeletionPolicyPort.deleteCancelledByClientId(clientId);
		deleteClientPort.deleteById(clientId);
	}
}


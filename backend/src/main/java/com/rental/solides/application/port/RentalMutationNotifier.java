package com.rental.solides.application.port;

/**
 * Notifica a infraestrutura para invalidar caches após mudança em aluguéis.
 */
public interface RentalMutationNotifier {
	void onRentalChanged();
}

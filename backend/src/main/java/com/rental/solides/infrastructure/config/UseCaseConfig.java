package com.rental.solides.infrastructure.config;

import com.rental.solides.application.port.AvailableVehiclesQueryPort;
import com.rental.solides.application.port.DeleteClientPort;
import com.rental.solides.application.port.DeleteVehiclePort;
import com.rental.solides.application.port.LoadClientPort;
import com.rental.solides.application.port.LoadRentalPort;
import com.rental.solides.application.port.LoadVehiclePort;
import com.rental.solides.application.port.RentalDeletionPolicyPort;
import com.rental.solides.application.port.RentalMutationNotifier;
import com.rental.solides.application.port.RentalOverlapPort;
import com.rental.solides.application.port.SaveClientPort;
import com.rental.solides.application.port.SaveRentalPort;
import com.rental.solides.application.port.SaveVehiclePort;
import com.rental.solides.application.usecase.CancelRentalUseCase;
import com.rental.solides.application.usecase.CreateClientUseCase;
import com.rental.solides.application.usecase.CreateRentalUseCase;
import com.rental.solides.application.usecase.CreateVehicleUseCase;
import com.rental.solides.application.usecase.DeleteClientUseCase;
import com.rental.solides.application.usecase.DeleteVehicleUseCase;
import com.rental.solides.application.usecase.GetClientUseCase;
import com.rental.solides.application.usecase.GetRentalUseCase;
import com.rental.solides.application.usecase.GetVehicleUseCase;
import com.rental.solides.application.usecase.ListAvailableVehiclesUseCase;
import com.rental.solides.application.usecase.ListClientsUseCase;
import com.rental.solides.application.usecase.ListRentalsUseCase;
import com.rental.solides.application.usecase.ListVehiclesUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

	@Bean
	public CreateVehicleUseCase createVehicleUseCase(SaveVehiclePort saveVehiclePort) {
		return new CreateVehicleUseCase(saveVehiclePort);
	}

	@Bean
	public ListVehiclesUseCase listVehiclesUseCase(LoadVehiclePort loadVehiclePort) {
		return new ListVehiclesUseCase(loadVehiclePort);
	}

	@Bean
	public GetVehicleUseCase getVehicleUseCase(LoadVehiclePort loadVehiclePort) {
		return new GetVehicleUseCase(loadVehiclePort);
	}

	@Bean
	public DeleteVehicleUseCase deleteVehicleUseCase(LoadVehiclePort loadVehiclePort, DeleteVehiclePort deleteVehiclePort,
			RentalDeletionPolicyPort rentalDeletionPolicyPort, RentalMutationNotifier rentalMutationNotifier) {
		return new DeleteVehicleUseCase(loadVehiclePort, deleteVehiclePort, rentalDeletionPolicyPort,
				rentalMutationNotifier);
	}

	@Bean
	public ListAvailableVehiclesUseCase listAvailableVehiclesUseCase(
			AvailableVehiclesQueryPort availableVehiclesQueryPort) {
		return new ListAvailableVehiclesUseCase(availableVehiclesQueryPort);
	}

	@Bean
	public CreateClientUseCase createClientUseCase(SaveClientPort saveClientPort) {
		return new CreateClientUseCase(saveClientPort);
	}

	@Bean
	public ListClientsUseCase listClientsUseCase(LoadClientPort loadClientPort) {
		return new ListClientsUseCase(loadClientPort);
	}

	@Bean
	public GetClientUseCase getClientUseCase(LoadClientPort loadClientPort) {
		return new GetClientUseCase(loadClientPort);
	}

	@Bean
	public DeleteClientUseCase deleteClientUseCase(LoadClientPort loadClientPort, DeleteClientPort deleteClientPort,
			RentalDeletionPolicyPort rentalDeletionPolicyPort) {
		return new DeleteClientUseCase(loadClientPort, deleteClientPort, rentalDeletionPolicyPort);
	}

	@Bean
	public CreateRentalUseCase createRentalUseCase(LoadVehiclePort loadVehiclePort, LoadClientPort loadClientPort,
			RentalOverlapPort rentalOverlapPort, SaveRentalPort saveRentalPort,
			RentalMutationNotifier rentalMutationNotifier) {
		return new CreateRentalUseCase(loadVehiclePort, loadClientPort, rentalOverlapPort, saveRentalPort,
				rentalMutationNotifier);
	}

	@Bean
	public ListRentalsUseCase listRentalsUseCase(LoadRentalPort loadRentalPort) {
		return new ListRentalsUseCase(loadRentalPort);
	}

	@Bean
	public GetRentalUseCase getRentalUseCase(LoadRentalPort loadRentalPort) {
		return new GetRentalUseCase(loadRentalPort);
	}

	@Bean
	public CancelRentalUseCase cancelRentalUseCase(LoadRentalPort loadRentalPort, SaveRentalPort saveRentalPort,
			RentalMutationNotifier rentalMutationNotifier) {
		return new CancelRentalUseCase(loadRentalPort, saveRentalPort, rentalMutationNotifier);
	}
}

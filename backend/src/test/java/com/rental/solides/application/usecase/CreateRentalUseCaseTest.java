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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRentalUseCaseTest {

	@Mock
	private LoadVehiclePort loadVehiclePort;
	@Mock
	private LoadClientPort loadClientPort;
	@Mock
	private RentalOverlapPort rentalOverlapPort;
	@Mock
	private SaveRentalPort saveRentalPort;
	@Mock
	private RentalMutationNotifier rentalMutationNotifier;

	private CreateRentalUseCase useCase;

	private final UUID vehicleId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();

	@BeforeEach
	void setUp() {
		useCase = new CreateRentalUseCase(loadVehiclePort, loadClientPort, rentalOverlapPort, saveRentalPort,
				rentalMutationNotifier);
	}

	@Test
	void execute_whenOverlap_throwsBusinessException() {
		Vehicle v = Vehicle.reconstitute(vehicleId, "ABC1D23", "Fiat", "Mobi", 2022, new BigDecimal("100"));
		Client c = Client.reconstitute(clientId, "João", "12345678909", "j@e.com", null);
		when(loadVehiclePort.findById(vehicleId)).thenReturn(Optional.of(v));
		when(loadClientPort.findById(clientId)).thenReturn(Optional.of(c));
		when(rentalOverlapPort.existsActiveOverlap(eq(vehicleId), any(), any(), isNull())).thenReturn(true);

		assertThatThrownBy(() -> useCase.execute(vehicleId, clientId, LocalDate.now().plusDays(1),
				LocalDate.now().plusDays(3))).isInstanceOf(BusinessException.class)
				.hasMessageContaining("indisponível");

		verify(saveRentalPort, never()).save(any());
		verify(rentalMutationNotifier, never()).onRentalChanged();
	}

	@Test
	void execute_vehicleNotFound_throws() {
		when(loadVehiclePort.findById(vehicleId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> useCase.execute(vehicleId, clientId, LocalDate.now(), LocalDate.now()))
				.isInstanceOf(ResourceNotFoundException.class);
	}
}

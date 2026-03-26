package com.rental.solides.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class RentalRequestDto {

	@NotNull
	private UUID vehicleId;

	@NotNull
	private UUID clientId;

	@NotNull
	private LocalDate startDate;

	@NotNull
	private LocalDate endDate;
}

package com.rental.solides.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleRequestDto {

	@NotBlank
	private String plate;

	@NotBlank
	private String brand;

	@NotBlank
	private String model;

	private Integer year;

	@NotNull
	@DecimalMin(value = "0.01", inclusive = true, message = "Diária deve ser maior que zero")
	private BigDecimal dailyRate;
}

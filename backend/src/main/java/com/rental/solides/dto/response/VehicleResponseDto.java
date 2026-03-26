package com.rental.solides.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleResponseDto {
	private String id;
	private String plate;
	private String brand;
	private String model;
	private Integer year;
	private BigDecimal dailyRate;
}

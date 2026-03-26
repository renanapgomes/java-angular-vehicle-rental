package com.rental.solides.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Vehicle {

	private UUID id;
	private final String plate;
	private final String brand;
	private final String model;
	private final Integer year;
	private final BigDecimal dailyRate;

	public static Vehicle create(String plate, String brand, String model, Integer year, BigDecimal dailyRate) {
		String normalizedPlate = normalizePlate(plate);
		validateBrandModel(brand, model);
		validateDailyRate(dailyRate);
		validateYear(year);
		return new Vehicle(null, normalizedPlate, brand.trim(), model.trim(), year, dailyRate);
	}

	public static Vehicle reconstitute(UUID id, String plate, String brand, String model, Integer year,
			BigDecimal dailyRate) {
		return new Vehicle(id, plate, brand, model, year, dailyRate);
	}

	public void setId(UUID id) {
		this.id = id;
	}

	private static String normalizePlate(String plate) {
		if (plate == null || plate.isBlank()) {
			throw new BusinessException("A placa é obrigatória");
		}
		String limpa = plate.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
		if (limpa.length() < 7) {
			throw new BusinessException("Placa inválida (mínimo 7 caracteres alfanuméricos)");
		}
		return limpa;
	}

	private static void validateBrandModel(String brand, String model) {
		if (brand == null || brand.isBlank()) {
			throw new BusinessException("A marca é obrigatória");
		}
		if (model == null || model.isBlank()) {
			throw new BusinessException("O modelo é obrigatório");
		}
	}

	private static void validateDailyRate(BigDecimal dailyRate) {
		if (dailyRate == null) {
			throw new BusinessException("O valor da diária é obrigatório");
		}
		if (dailyRate.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException("O valor da diária deve ser maior que zero");
		}
	}

	private static void validateYear(Integer year) {
		if (year != null && (year < 1900 || year > 2100)) {
			throw new BusinessException("Ano do veículo inválido");
		}
	}
}

package com.rental.solides.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Agregado de aluguel: cálculo de dias (inclusivo), valor total e checagem de sobreposição de períodos.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Rental {

	private UUID id;
	private final UUID vehicleId;
	private final UUID clientId;
	private final LocalDate startDate;
	private final LocalDate endDate;
	private final BigDecimal totalAmount;
	private RentalStatus status;

	public static long countInclusiveRentalDays(LocalDate start, LocalDate end) {
		if (start == null || end == null) {
			throw new BusinessException("Datas de início e fim são obrigatórias");
		}
		if (end.isBefore(start)) {
			throw new BusinessException("A data final não pode ser anterior à data inicial");
		}
		return ChronoUnit.DAYS.between(start, end) + 1;
	}

	public static BigDecimal calculateTotal(BigDecimal dailyRate, long numberOfDays) {
		if (dailyRate == null || dailyRate.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException("Diária inválida para cálculo");
		}
		if (numberOfDays < 1) {
			throw new BusinessException("Período deve ter pelo menos 1 dia");
		}
		return dailyRate.multiply(BigDecimal.valueOf(numberOfDays)).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Períodos inclusivos [start, end] se sobrepõem se compartilham ao menos um dia.
	 */
	public static boolean periodsOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
		if (start1 == null || end1 == null || start2 == null || end2 == null) {
			return false;
		}
		return !start1.isAfter(end2) && !start2.isAfter(end1);
	}

	public static Rental create(UUID vehicleId, UUID clientId, LocalDate startDate, LocalDate endDate,
			BigDecimal vehicleDailyRate) {
		if (vehicleId == null || clientId == null) {
			throw new BusinessException("Veículo e cliente são obrigatórios");
		}
		long days = countInclusiveRentalDays(startDate, endDate);
		BigDecimal total = calculateTotal(vehicleDailyRate, days);
		return new Rental(null, vehicleId, clientId, startDate, endDate, total, RentalStatus.ACTIVE);
	}

	public static Rental reconstitute(UUID id, UUID vehicleId, UUID clientId, LocalDate startDate,
			LocalDate endDate, BigDecimal totalAmount, RentalStatus status) {
		return new Rental(id, vehicleId, clientId, startDate, endDate, totalAmount, status);
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void cancel() {
		if (this.status == RentalStatus.CANCELLED) {
			throw new BusinessException("Aluguel já está cancelado");
		}
		this.status = RentalStatus.CANCELLED;
	}
}

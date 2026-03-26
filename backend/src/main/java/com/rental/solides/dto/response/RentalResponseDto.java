package com.rental.solides.dto.response;

import com.rental.solides.domain.RentalStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RentalResponseDto {
	private String id;
	private String vehicleId;
	private String vehiclePlate;
	private String clientId;
	private String clientName;
	private LocalDate startDate;
	private LocalDate endDate;
	private BigDecimal totalAmount;
	private RentalStatus status;
}

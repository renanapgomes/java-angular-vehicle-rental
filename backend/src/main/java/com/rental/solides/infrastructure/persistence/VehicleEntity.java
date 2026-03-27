package com.rental.solides.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "vehicles", indexes = @Index(columnList = "plate", unique = true))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, unique = true, length = 20)
	private String plate;

	@Column(nullable = false, length = 120)
	private String brand;

	@Column(nullable = false, length = 120)
	private String model;

	@Column(name = "model_year")
	private Integer year;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal dailyRate;
}

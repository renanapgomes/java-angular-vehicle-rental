package com.rental.solides.infrastructure.persistence;

import com.rental.solides.domain.RentalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "rentals", indexes = {
		@Index(columnList = "vehicle_id"),
		@Index(columnList = "client_id"),
		@Index(columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "vehicle_id", nullable = false)
	private VehicleEntity vehicle;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", nullable = false)
	private ClientEntity client;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Column(nullable = false, precision = 14, scale = 2)
	private BigDecimal totalAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private RentalStatus status = RentalStatus.ACTIVE;
}

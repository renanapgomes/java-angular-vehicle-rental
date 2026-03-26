package com.rental.solides.infrastructure.persistence;

import com.rental.solides.domain.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<VehicleEntity, UUID> {

	@Query("""
			SELECT v FROM VehicleEntity v
			WHERE NOT EXISTS (
				SELECT 1 FROM RentalEntity r
				WHERE r.vehicle.id = v.id
				AND r.status = :active
				AND r.startDate <= :end
				AND r.endDate >= :start
			)
			ORDER BY v.brand, v.model
			""")
	List<VehicleEntity> findAvailableInPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end,
			@Param("active") RentalStatus active);
}

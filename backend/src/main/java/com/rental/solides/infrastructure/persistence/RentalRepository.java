package com.rental.solides.infrastructure.persistence;

import com.rental.solides.domain.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentalRepository extends JpaRepository<RentalEntity, UUID> {

	List<RentalEntity> findAllByOrderByStartDateDesc();

	@Query("""
			SELECT COUNT(r) > 0 FROM RentalEntity r
			WHERE r.vehicle.id = :vehicleId
			AND r.status = :status
			AND r.startDate <= :end
			AND r.endDate >= :start
			AND (:excludeId IS NULL OR r.id <> :excludeId)
			""")
	boolean existsActiveOverlap(@Param("vehicleId") UUID vehicleId,
			@Param("start") LocalDate start,
			@Param("end") LocalDate end,
			@Param("excludeId") UUID excludeRentalId,
			@Param("status") RentalStatus status);

	boolean existsByVehicle_IdAndStatus(UUID vehicleId, RentalStatus status);

	boolean existsByClient_IdAndStatus(UUID clientId, RentalStatus status);

	void deleteByVehicle_IdAndStatus(UUID vehicleId, RentalStatus status);

	void deleteByClient_IdAndStatus(UUID clientId, RentalStatus status);
}

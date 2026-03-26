package com.rental.solides.infrastructure.web;

import com.rental.solides.application.port.LoadClientPort;
import com.rental.solides.application.port.LoadVehiclePort;
import com.rental.solides.domain.Client;
import com.rental.solides.domain.Rental;
import com.rental.solides.domain.Vehicle;
import com.rental.solides.dto.response.RentalResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RentalWebMapper {

	private final LoadVehiclePort loadVehiclePort;
	private final LoadClientPort loadClientPort;

	public RentalWebMapper(LoadVehiclePort loadVehiclePort, LoadClientPort loadClientPort) {
		this.loadVehiclePort = loadVehiclePort;
		this.loadClientPort = loadClientPort;
	}

	public RentalResponseDto toResponseDto(Rental r) {
		RentalResponseDto dto = new RentalResponseDto();
		dto.setId(r.getId() != null ? r.getId().toString() : null);
		dto.setVehicleId(r.getVehicleId().toString());
		dto.setClientId(r.getClientId().toString());
		dto.setStartDate(r.getStartDate());
		dto.setEndDate(r.getEndDate());
		dto.setTotalAmount(r.getTotalAmount());
		dto.setStatus(r.getStatus());

		loadVehiclePort.findById(r.getVehicleId()).map(Vehicle::getPlate).ifPresent(dto::setVehiclePlate);
		loadClientPort.findById(r.getClientId()).map(Client::getFullName).ifPresent(dto::setClientName);

		return dto;
	}
}

package com.rental.solides.infrastructure.web;

import com.rental.solides.domain.Vehicle;
import com.rental.solides.dto.response.VehicleResponseDto;
import org.springframework.stereotype.Component;

@Component
public class VehicleWebMapper {

	public VehicleResponseDto toResponseDto(Vehicle v) {
		VehicleResponseDto dto = new VehicleResponseDto();
		dto.setId(v.getId() != null ? v.getId().toString() : null);
		dto.setPlate(v.getPlate());
		dto.setBrand(v.getBrand());
		dto.setModel(v.getModel());
		dto.setYear(v.getYear());
		dto.setDailyRate(v.getDailyRate());
		return dto;
	}
}

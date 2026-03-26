package com.rental.solides.infrastructure.web;

import com.rental.solides.application.usecase.CancelRentalUseCase;
import com.rental.solides.application.usecase.CreateRentalUseCase;
import com.rental.solides.application.usecase.GetRentalUseCase;
import com.rental.solides.application.usecase.ListRentalsUseCase;
import com.rental.solides.domain.Rental;
import com.rental.solides.dto.request.RentalRequestDto;
import com.rental.solides.dto.response.RentalResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Aluguéis")
public class RentalController {

	private final CreateRentalUseCase createRentalUseCase;
	private final ListRentalsUseCase listRentalsUseCase;
	private final GetRentalUseCase getRentalUseCase;
	private final CancelRentalUseCase cancelRentalUseCase;
	private final RentalWebMapper webMapper;

	public RentalController(CreateRentalUseCase createRentalUseCase, ListRentalsUseCase listRentalsUseCase,
			GetRentalUseCase getRentalUseCase, CancelRentalUseCase cancelRentalUseCase, RentalWebMapper webMapper) {
		this.createRentalUseCase = createRentalUseCase;
		this.listRentalsUseCase = listRentalsUseCase;
		this.getRentalUseCase = getRentalUseCase;
		this.cancelRentalUseCase = cancelRentalUseCase;
		this.webMapper = webMapper;
	}

	@PostMapping
	@Operation(summary = "Registrar aluguel (valor total calculado)")
	public ResponseEntity<RentalResponseDto> create(@Valid @RequestBody RentalRequestDto request) {
		Rental created = createRentalUseCase.execute(request.getVehicleId(), request.getClientId(),
				request.getStartDate(), request.getEndDate());
		return ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toResponseDto(created));
	}

	@GetMapping
	@Operation(summary = "Histórico de aluguéis")
	public List<RentalResponseDto> list() {
		return listRentalsUseCase.execute().stream().map(webMapper::toResponseDto).toList();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obter aluguel por id")
	public RentalResponseDto get(@PathVariable String id) {
		Rental r = getRentalUseCase.execute(parseUuid(id));
		return webMapper.toResponseDto(r);
	}

	@PostMapping("/{id}/cancel")
	@Operation(summary = "Cancelar aluguel (libera o veículo no período)")
	public RentalResponseDto cancel(@PathVariable String id) {
		Rental r = cancelRentalUseCase.execute(parseUuid(id));
		return webMapper.toResponseDto(r);
	}

	private static UUID parseUuid(String id) {
		try {
			return UUID.fromString(id);
		} catch (Exception e) {
			throw new IllegalArgumentException("ID inválido: " + id);
		}
	}
}

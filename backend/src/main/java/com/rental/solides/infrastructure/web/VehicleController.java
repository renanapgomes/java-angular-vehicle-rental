package com.rental.solides.infrastructure.web;

import com.rental.solides.application.usecase.CreateVehicleUseCase;
import com.rental.solides.application.usecase.DeleteVehicleUseCase;
import com.rental.solides.application.usecase.GetVehicleUseCase;
import com.rental.solides.application.usecase.ListAvailableVehiclesUseCase;
import com.rental.solides.application.usecase.ListVehiclesUseCase;
import com.rental.solides.domain.Vehicle;
import com.rental.solides.dto.request.VehicleRequestDto;
import com.rental.solides.dto.response.VehicleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Veículos")
public class VehicleController {

	private final CreateVehicleUseCase createVehicleUseCase;
	private final ListVehiclesUseCase listVehiclesUseCase;
	private final GetVehicleUseCase getVehicleUseCase;
	private final DeleteVehicleUseCase deleteVehicleUseCase;
	private final ListAvailableVehiclesUseCase listAvailableVehiclesUseCase;
	private final VehicleWebMapper webMapper;

	public VehicleController(CreateVehicleUseCase createVehicleUseCase, ListVehiclesUseCase listVehiclesUseCase,
			GetVehicleUseCase getVehicleUseCase, DeleteVehicleUseCase deleteVehicleUseCase,
			ListAvailableVehiclesUseCase listAvailableVehiclesUseCase, VehicleWebMapper webMapper) {
		this.createVehicleUseCase = createVehicleUseCase;
		this.listVehiclesUseCase = listVehiclesUseCase;
		this.getVehicleUseCase = getVehicleUseCase;
		this.deleteVehicleUseCase = deleteVehicleUseCase;
		this.listAvailableVehiclesUseCase = listAvailableVehiclesUseCase;
		this.webMapper = webMapper;
	}

	@PostMapping
	@Operation(summary = "Cadastrar veículo")
	public ResponseEntity<VehicleResponseDto> create(@Valid @RequestBody VehicleRequestDto request) {
		Vehicle created = createVehicleUseCase.execute(request.getPlate(), request.getBrand(), request.getModel(),
				request.getYear(), request.getDailyRate());
		return ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toResponseDto(created));
	}

	@GetMapping
	@Operation(summary = "Listar veículos")
	public List<VehicleResponseDto> list() {
		return listVehiclesUseCase.execute().stream().map(webMapper::toResponseDto).toList();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obter veículo por id")
	public VehicleResponseDto get(@PathVariable String id) {
		Vehicle v = getVehicleUseCase.execute(parseUuid(id));
		return webMapper.toResponseDto(v);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir veículo (somente sem aluguel ativo; histórico cancelado pode ser removido)")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		deleteVehicleUseCase.execute(parseUuid(id));
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/available")
	@Operation(summary = "Listar veículos disponíveis no período (sem aluguel ativo conflitante)")
	public List<VehicleResponseDto> available(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return listAvailableVehiclesUseCase.execute(from, to).stream().map(webMapper::toResponseDto).toList();
	}

	private static UUID parseUuid(String id) {
		try {
			return UUID.fromString(id);
		} catch (Exception e) {
			throw new IllegalArgumentException("ID inválido: " + id);
		}
	}
}

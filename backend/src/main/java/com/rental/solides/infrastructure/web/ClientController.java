package com.rental.solides.infrastructure.web;

import com.rental.solides.application.usecase.CreateClientUseCase;
import com.rental.solides.application.usecase.DeleteClientUseCase;
import com.rental.solides.application.usecase.GetClientUseCase;
import com.rental.solides.application.usecase.ListClientsUseCase;
import com.rental.solides.domain.Client;
import com.rental.solides.dto.request.ClientRequestDto;
import com.rental.solides.dto.response.ClientResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clientes")
public class ClientController {

	private final CreateClientUseCase createClientUseCase;
	private final ListClientsUseCase listClientsUseCase;
	private final GetClientUseCase getClientUseCase;
	private final DeleteClientUseCase deleteClientUseCase;
	private final ClientWebMapper webMapper;

	public ClientController(CreateClientUseCase createClientUseCase, ListClientsUseCase listClientsUseCase,
			GetClientUseCase getClientUseCase, DeleteClientUseCase deleteClientUseCase, ClientWebMapper webMapper) {
		this.createClientUseCase = createClientUseCase;
		this.listClientsUseCase = listClientsUseCase;
		this.getClientUseCase = getClientUseCase;
		this.deleteClientUseCase = deleteClientUseCase;
		this.webMapper = webMapper;
	}

	@PostMapping
	@Operation(summary = "Cadastrar cliente")
	public ResponseEntity<ClientResponseDto> create(@Valid @RequestBody ClientRequestDto request) {
		Client created = createClientUseCase.execute(request.getFullName(), request.getDocument(), request.getEmail(),
				request.getPhone());
		return ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toResponseDto(created));
	}

	@GetMapping
	@Operation(summary = "Listar clientes")
	public List<ClientResponseDto> list() {
		return listClientsUseCase.execute().stream().map(webMapper::toResponseDto).toList();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obter cliente por id")
	public ClientResponseDto get(@PathVariable String id) {
		Client c = getClientUseCase.execute(parseUuid(id));
		return webMapper.toResponseDto(c);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir cliente (somente sem aluguel ativo; histórico cancelado pode ser removido)")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		deleteClientUseCase.execute(parseUuid(id));
		return ResponseEntity.noContent().build();
	}

	private static UUID parseUuid(String id) {
		try {
			return UUID.fromString(id);
		} catch (Exception e) {
			throw new IllegalArgumentException("ID inválido: " + id);
		}
	}
}

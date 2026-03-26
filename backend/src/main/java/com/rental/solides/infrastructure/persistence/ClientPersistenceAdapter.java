package com.rental.solides.infrastructure.persistence;

import com.rental.solides.application.port.LoadClientPort;
import com.rental.solides.application.port.SaveClientPort;
import com.rental.solides.application.port.DeleteClientPort;
import com.rental.solides.domain.Client;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ClientPersistenceAdapter implements SaveClientPort, LoadClientPort, DeleteClientPort {

	private final ClientRepository clientRepository;

	public ClientPersistenceAdapter(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public Client save(Client client) {
		ClientEntity entity = toEntity(client);
		ClientEntity saved = clientRepository.save(entity);
		return toDomain(saved);
	}

	@Override
	public Optional<Client> findById(UUID id) {
		return clientRepository.findById(id).map(this::toDomain);
	}

	@Override
	public List<Client> findAll() {
		return clientRepository.findAll().stream().map(this::toDomain).toList();
	}

	@Override
	public void deleteById(UUID id) {
		clientRepository.deleteById(id);
	}

	private ClientEntity toEntity(Client c) {
		ClientEntity e = new ClientEntity();
		e.setId(c.getId());
		e.setFullName(c.getFullName());
		e.setDocument(c.getDocument());
		e.setEmail(c.getEmail());
		e.setPhone(c.getPhone());
		return e;
	}

	private Client toDomain(ClientEntity e) {
		return Client.reconstitute(e.getId(), e.getFullName(), e.getDocument(), e.getEmail(), e.getPhone());
	}
}

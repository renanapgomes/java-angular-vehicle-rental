package com.rental.solides.application.port;

import com.rental.solides.domain.Client;

public interface SaveClientPort {
	Client save(Client client);
}

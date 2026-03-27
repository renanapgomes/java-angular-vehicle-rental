package com.rental.solides.infrastructure.config;

import com.rental.solides.application.port.SaveClientPort;
import com.rental.solides.application.port.SaveVehiclePort;
import com.rental.solides.domain.Client;
import com.rental.solides.domain.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@Configuration
@Profile("dev")
public class SampleDataLoader {

	private static final Logger log = LoggerFactory.getLogger(SampleDataLoader.class);

	@Bean
	CommandLineRunner loadSamples(SaveVehiclePort saveVehiclePort, SaveClientPort saveClientPort) {
		return args -> {
			Vehicle v1 = saveVehiclePort.save(
					Vehicle.create("ABC1D23", "Fiat", "Mobi", 2022, new BigDecimal("100.00")));
			Vehicle v2 = saveVehiclePort.save(
					Vehicle.create("XYZ9W87", "Volkswagen", "Polo", 2023, new BigDecimal("150.50")));
			Client c1 = saveClientPort.save(Client.create("Maria Silva", "12345678909", "maria@email.com", "11999990000"));
			log.info("Dados de exemplo carregados: veículos {} e {}, cliente {}.", v1.getPlate(), v2.getPlate(),
					c1.getFullName());
		};
	}
}

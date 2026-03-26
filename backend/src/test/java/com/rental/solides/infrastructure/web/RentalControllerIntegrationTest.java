package com.rental.solides.infrastructure.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class RentalControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void create_rental_whenValid_returns201() throws Exception {
		String vehiclePlate = newPlate();
		String vehicleId = createVehicle(vehiclePlate, 120.00);

		String fullName = newFullName();
		String document = newDocument();
		String email = newEmail(fullName);
		String clientId = createClient(fullName, document, email, "11999990000");

		String payload = """
		{
		  "vehicleId": "%s",
		  "clientId": "%s",
		  "startDate": "2026-03-25",
		  "endDate": "2026-03-28"
		}
		""".formatted(vehicleId, clientId);

		mockMvc.perform(post("/api/rentals").contentType(MediaType.APPLICATION_JSON).content(payload))
				.andExpect(status().isCreated());
	}

	@Test
	void create_rental_whenOverlapsActiveRental_returns400() throws Exception {
		String vehicleId = createVehicle(newPlate(), 150.50);

		String c1FullName = newFullName();
		String c1Id = createClient(c1FullName, newDocument(), newEmail(c1FullName), "11999990000");

		String c2FullName = newFullName();
		String c2Id = createClient(c2FullName, newDocument(), newEmail(c2FullName), null);

		// Rental 1: 2026-03-25 -> 2026-03-28 (ACTIVE)
		String rental1 = """
		{
		  "vehicleId": "%s",
		  "clientId": "%s",
		  "startDate": "2026-03-25",
		  "endDate": "2026-03-28"
		}
		""".formatted(vehicleId, c1Id);

		mockMvc.perform(post("/api/rentals").contentType(MediaType.APPLICATION_JSON).content(rental1))
				.andExpect(status().isCreated());

		// Rental 2 (sobrepõe): 2026-03-27 -> 2026-03-30
		String rental2 = """
		{
		  "vehicleId": "%s",
		  "clientId": "%s",
		  "startDate": "2026-03-27",
		  "endDate": "2026-03-30"
		}
		""".formatted(vehicleId, c2Id);

		MvcResult result = mockMvc.perform(post("/api/rentals").contentType(MediaType.APPLICATION_JSON).content(rental2))
				.andExpect(status().isBadRequest())
				.andReturn();

		String body = result.getResponse().getContentAsString();
		// GlobalExceptionHandler devolve "message" com a mensagem do BusinessException.
		// Como a string está em JSON, buscamos apenas a substring.
		org.assertj.core.api.Assertions.assertThat(body).contains("indispon");
	}

	private String createVehicle(String plate, double dailyRate) throws Exception {
		String payload = """
		{
		  "plate": "%s",
		  "brand": "Fiat",
		  "model": "Mobi",
		  "year": 2022,
		  "dailyRate": %s
		}
		""".formatted(plate, dailyRate);

		MvcResult result = mockMvc.perform(post("/api/vehicles").contentType(MediaType.APPLICATION_JSON).content(payload))
				.andExpect(status().isCreated())
				.andReturn();
		JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
		return json.get("id").asText();
	}

	private String createClient(String fullName, String document, String email, String phone) throws Exception {
		String payload = """
		{
		  "fullName": "%s",
		  "document": "%s",
		  "email": "%s",
		  "phone": %s
		}
		""".formatted(fullName, document, email, phone == null ? "null" : "\"" + phone + "\"");

		MvcResult result = mockMvc.perform(post("/api/clients").contentType(MediaType.APPLICATION_JSON).content(payload))
				.andExpect(status().isCreated())
				.andReturn();
		JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
		return json.get("id").asText();
	}

	private String newPlate() {
		// Gera uma placa unica (>= 7 caracteres alfanuméricos) para evitar colisões com dados de exemplo.
		String hex = java.util.UUID.randomUUID().toString().replace("-", "");
		return ("TST" + hex.substring(0, 4)).toUpperCase();
	}

	private String newDocument() {
		// CPF sem máscara: exatamente 11 dígitos (validação no domínio exige 11 dígitos).
		long min = 10_000_000_000L; // 11 dígitos (10.000.000.000)
		long maxInclusive = 99_999_999_999L; // 11 dígitos
		long value = java.util.concurrent.ThreadLocalRandom.current().nextLong(min, maxInclusive + 1);
		return String.valueOf(value);
	}

	private String newFullName() {
		String hex = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		return "Cliente " + hex;
	}

	private String newEmail(String fullName) {
		// Apenas para garantir que tem '@' e não colide.
		String hex = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		return "cliente" + hex.toLowerCase() + "@email.com";
	}
}


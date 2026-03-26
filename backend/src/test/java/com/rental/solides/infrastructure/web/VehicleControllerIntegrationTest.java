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

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class VehicleControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void create_vehicle_whenInvalid_returns400() throws Exception {
		String payload = """
		{
		  "plate": "",
		  "brand": "",
		  "model": "",
		  "year": 2022,
		  "dailyRate": 0
		}
		""";

		mockMvc.perform(post("/api/vehicles").contentType(MediaType.APPLICATION_JSON).content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.message", notNullValue()));
	}

	@Test
	void create_vehicle_whenValid_returns201_andReturnsId() throws Exception {
		String plate = newPlate();
		String payload = """
		{
		  "plate": "%s",
		  "brand": "Fiat",
		  "model": "Mobi",
		  "year": 2022,
		  "dailyRate": 120.00
		}
		""".formatted(plate);

		MvcResult result = mockMvc.perform(post("/api/vehicles").contentType(MediaType.APPLICATION_JSON).content(payload))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andReturn();

		String body = result.getResponse().getContentAsString();
		JsonNode json = objectMapper.readTree(body);
		json.get("plate").asText(); // força leitura
	}

	private String newPlate() {
		// Gera uma placa unica (>= 7 caracteres alfanuméricos) para evitar colisões com dados de exemplo.
		String hex = java.util.UUID.randomUUID().toString().replace("-", "");
		return ("TST" + hex.substring(0, 4)).toUpperCase();
	}
}


package com.manuel.gym_api.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.manuel.gym_api.integration.dto.WgerResponseDTO;

import tools.jackson.databind.ObjectMapper;

@Component
public class WgerClient {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${wger.api.url}")
	private String wgerUrl;

	public WgerClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	public WgerResponseDTO getExercises() {
		try {
			// 1. Descargamos el JSON como texto plano
			String rawJson = restTemplate.getForObject(wgerUrl, String.class);

			// 2. Lo intentamos convertir manualmente para capturar el error exacto
			return objectMapper.readValue(rawJson, WgerResponseDTO.class);

		} catch (Exception e) {
			// Si falla, imprimimos el error exacto en rojo para saber qué campo está mal
			System.err.println("¡ERROR PARSEANDO EL JSON DE WGER!");
			e.printStackTrace();
			throw new RuntimeException("Error parseando Wger API: " + e.getMessage());
		}
	}
}
package com.manuel.gym_api.integration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WgerClient {

	private final RestTemplate restTemplate;

	@Value("${wger.api.url}")
	private String wgerUrl;

	public WgerClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	// Devuelve un mapa genérico, cero fallos de parseo.
	@SuppressWarnings("unchecked")
	public Map<String, Object> getExercises() {
		return restTemplate.getForObject(wgerUrl, Map.class);
	}
}
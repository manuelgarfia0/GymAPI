package com.manuel.gym_api.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.manuel.gym_api.integration.dto.WgerResponseDTO;

@Component
public class WgerClient {

	private final RestTemplate restTemplate;

	@Value("${wger.api.url}")
	private String wgerUrl;

	public WgerClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public WgerResponseDTO getExercises() {
		return restTemplate.getForObject(wgerUrl, WgerResponseDTO.class);
	}
}
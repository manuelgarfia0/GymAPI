package com.manuel.gym_api.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WgerClient {

	private final RestTemplate restTemplate = new RestTemplate();

	public String getExercises() {
		String url = "https://wger.de/api/v2/exercise/?language=2";
		return restTemplate.getForObject(url, String.class);
	}

}
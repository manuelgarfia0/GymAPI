package com.manuel.gym_api.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.manuel.gym_api.model.User;

@Service
public class TokenService {

	@Value("${api.security.secret:MySuperSecretGymAppKey123!}")
	private String apiSecret;
	@Value("${api.security.token.expiration-hours:24}")
	private int expirationHours;

	public String generateToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(apiSecret);
			return JWT.create().withIssuer("GymAPI") // Quien emite el token
					.withSubject(user.getUsername()) // A quién pertenece
					.withClaim("id", user.getId()) // Guardamos el ID para usarlo luego si lo necesitamos
					.withExpiresAt(generateExpirationDate()) // Cuándo caduca
					.sign(algorithm);
		} catch (JWTCreationException exception) {
			throw new RuntimeException("Error while generating token", exception);
		}
	}

	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(apiSecret);
			return JWT.require(algorithm).withIssuer("GymAPI").build().verify(token).getSubject(); // Devuelve el
																									// username si el
																									// token es válido
		} catch (JWTVerificationException exception) {
			return ""; // Devuelve vacío si el token es falso o expiró
		}
	}

	// El token durará 24 horas (puedes cambiarlo a lo que prefieras para la app
	// móvil)
	private Instant generateExpirationDate() {
		return Instant.now().plus(expirationHours, ChronoUnit.HOURS);
	}
}
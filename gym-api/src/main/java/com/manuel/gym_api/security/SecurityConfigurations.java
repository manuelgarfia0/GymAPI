package com.manuel.gym_api.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

	private final SecurityFilter securityFilter;
	@Value("${cors.allowed-origins}")
	private String allowedOrigins;

	public SecurityConfigurations(SecurityFilter securityFilter) {
		this.securityFilter = securityFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						// Permitir endpoints de autenticación sin JWT
						.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()

						// Permitir exercises temporalmente
						.requestMatchers("/api/exercises").permitAll()

						// REQUERIR JWT para /api/auth/me y otros endpoints protegidos
						.requestMatchers("/api/auth/me").authenticated()

						// Requerir autenticación para todo lo demás
						.anyRequest().authenticated())
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// Configuración específica para emulador Android y desarrollo
		configuration.setAllowedOriginPatterns(Arrays.asList(allowedOrigins.split(",")));

		// Permitir todos los métodos HTTP necesarios
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

		// Permitir headers específicos para autenticación JWT
		configuration.setAllowedHeaders(Arrays.asList("*", // Permitir todos los headers
				"Authorization", // Header JWT
				"Content-Type", // Tipo de contenido
				"Accept", // Aceptar respuesta
				"Origin", // Origen de la petición
				"Access-Control-Request-Method", // Método de la petición CORS
				"Access-Control-Request-Headers" // Headers de la petición CORS
		));

		// Exponer headers de respuesta necesarios
		configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Access-Control-Allow-Origin",
				"Access-Control-Allow-Credentials"));

		// Permitir credenciales (necesario para JWT)
		configuration.setAllowCredentials(true);

		// Configurar tiempo de cache para preflight requests
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
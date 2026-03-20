package com.manuel.gym_api.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	private final TokenService tokenService;
	private final AuthService authService;

	public SecurityFilter(TokenService tokenService, AuthService authService) {
		this.tokenService = tokenService;
		this.authService = authService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var token = this.recoverToken(request);

		if (token != null) {
			// El token devuelve el username si es válido, si no, devuelve vacío ""
			var username = tokenService.validateToken(token);

			if (!username.isEmpty()) {
				// Buscamos al usuario en la BD
				UserDetails user = authService.loadUserByUsername(username);

				if (user != null) {
					// Si existe, le decimos a Spring que el usuario está autenticado
					var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}

		// Pasamos la petición al siguiente filtro (o al controlador si ya terminó)
		filterChain.doFilter(request, response);
	}

	private String recoverToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		if (authHeader == null)
			return null;

		if (!authHeader.startsWith("Bearer "))
			return null;
		return authHeader.substring(7);
	}
}
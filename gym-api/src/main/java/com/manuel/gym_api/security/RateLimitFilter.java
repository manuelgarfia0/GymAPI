package com.manuel.gym_api.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component

public class RateLimitFilter extends OncePerRequestFilter {

	// 10 intentos por IP cada 15 minutos

	private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

	private Bucket getBucket(String ip) {

		return buckets.computeIfAbsent(ip, k ->

		Bucket.builder()

				.addLimit(Bandwidth.builder()

						.capacity(10)

						.refillIntervally(10, Duration.ofMinutes(15))

						.build())

				.build());

	}

	@Override

	protected void doFilterInternal(HttpServletRequest request,

			HttpServletResponse response, FilterChain chain)

			throws ServletException, IOException {

		if ("/api/auth/login".equals(request.getRequestURI())

				&& "POST".equals(request.getMethod())) {

			String ip = request.getRemoteAddr();

			if (!getBucket(ip).tryConsume(1)) {

				response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

				response.getWriter().write(

						"{\"error\":\"Too many login attempts. Try again later.\"}");

				return;

			}

		}

		chain.doFilter(request, response);

	}

}
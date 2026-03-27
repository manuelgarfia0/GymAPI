package com.manuel.gym_api.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
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

	private static final int MAX_BUCKETS = 10_000;
	private static final long EVICTION_MS = 15 * 60 * 1_000L;

	private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
	private final Map<String, Long> lastSeen = new ConcurrentHashMap<>();

	private Bucket getBucket(String ip) {
		lastSeen.put(ip, System.currentTimeMillis());

		// Evictar entradas antiguas si el mapa crece demasiado
		if (buckets.size() > MAX_BUCKETS) {
			evictStaleEntries();
		}

		return buckets.computeIfAbsent(ip, k ->
				Bucket.builder()
						.addLimit(Bandwidth.builder()
								.capacity(10)
								.refillIntervally(10, Duration.ofMinutes(15))
								.build())
						.build()
		);
	}

	private void evictStaleEntries() {
		long cutoff = System.currentTimeMillis() - EVICTION_MS;
		Iterator<Map.Entry<String, Long>> it = lastSeen.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Long> entry = it.next();
			if (entry.getValue() < cutoff) {
				buckets.remove(entry.getKey());
				it.remove();
			}
		}
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
				response.setContentType("application/json");
				response.getWriter().write(
						"{\"error\":\"Too many login attempts. Try again later.\"}");
				return;
			}
		}

		chain.doFilter(request, response);
	}
}
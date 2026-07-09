/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Filter that intercepts all requests before authentication to apply rate limiting rules.
 * Created: 07/06/2026
 * Version: 1.0
 */

package org.bsr.springboot.practicebasicauth.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;


@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    private final Cache<String, Bucket> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(10))   // auto-evict idle IPs
            .maximumSize(10_000)                        // prevent memory blow-up
            .build();

    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(100)
                .refillIntervally(100, Duration.ofMinutes(1))
                .build();
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Autowired
    public RateLimitingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();

        Bucket bucket = cache.get(ip, value -> createBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        }
        else {
            response.setHeader("Retry-After", "60");
            response.setHeader("X-Content-Type-Options", "nosniff");
            response.setContentType("application/problem+json;charset=UTF-8");
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

            ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.TOO_MANY_REQUESTS);
//            problem.setType();
            problem.setDetail("Too many requests.");
            problem.setInstance(URI.create(request.getRequestURI()));
            problem.setProperty("timestamp", Instant.now().toString());

            objectMapper.writeValue(response.getWriter(), problem);
        }
    }
}

/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    RFC 9110 compliant AuthenticationEntryPoint with RFC 9457 Problem Details body
 * Created: 05/22/2026
 * Version: 1.0
 */

package org.bsr.springboot.practicebasicauth.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;


@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    private static final URI UNAUTHORIZED_TYPE =
            URI.create("not-a-real-uri/errors/unauthorized");
    @Autowired
    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // RFC 9110 — MUST send WWW-Authenticate
        response.setHeader("WWW-Authenticate", "Basic realm=\"api\"");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentType("application/problem+json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setType(UNAUTHORIZED_TYPE);
        problem.setDetail("Authentication credentials are missing or invalid. Include a valid Authorization header.");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", Instant.now().toString());

        objectMapper.writeValue(response.getWriter(), problem);
    }

}

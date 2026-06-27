/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Custom Access Denied handler to hide privileged endpoint existence on request when response would be 403
 * Created: 05/25/2026
 * Version: 1.0
 */

package org.bsr.springboot.practicebasicauth.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final SecurityAuditLoggerUtil auditLogger;
    private static final URI NOT_FOUND_TYPE = URI.create("not-a-real-uri/errors/not-found");


    @Autowired
    public CustomAccessDeniedHandler(ObjectMapper objectMapper, SecurityAuditLoggerUtil auditLogger) {
        this.objectMapper = objectMapper;
        this.auditLogger = auditLogger;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentType("application/problem+json;charset=UTF-8");

        response.setStatus(HttpStatus.NOT_FOUND.value());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setType(NOT_FOUND_TYPE);
        problem.setDetail("Resource not found.");
        problem.setProperty("timestamp", Instant.now().toString());

        objectMapper.writeValue(response.getWriter(), problem);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : "anonymous";
        auditLogger.logAuthzFailure(username,
                accessDeniedException.getLocalizedMessage(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
    }
}

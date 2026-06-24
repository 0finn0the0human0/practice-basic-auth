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
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
    private static final String APP_ID = "practice.basic-auth";
    private static final URI NOT_FOUND_TYPE = URI.create("not-a-real-uri/errors/not-found");


    @Autowired
    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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

        // #TODO reference username and a counter
        log.warn("authz_login_fail",
                StructuredArguments.keyValue("datetime", Instant.now().toString()),
                StructuredArguments.keyValue("appid", APP_ID),
                StructuredArguments.keyValue("event","authz_login_fail" + request),
                StructuredArguments.keyValue("level","CRITICAL"),
                StructuredArguments.keyValue("description", "Authz failure for user: "),
                StructuredArguments.keyValue("failure reason", accessDeniedException.getClass().getSimpleName()),
                StructuredArguments.keyValue("source_ip", request.getRemoteAddr()),
                StructuredArguments.keyValue("uri", request.getRequestURI()),
                StructuredArguments.keyValue("method", request.getMethod()));
    }
}

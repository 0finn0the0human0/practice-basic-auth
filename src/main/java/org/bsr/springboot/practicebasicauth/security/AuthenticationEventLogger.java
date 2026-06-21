/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Logs success and failure authentication events. Follows standardized logging format from OWASP Application
 *          Logging Vocabulary Cheat Sheet.
 * Created: 06/12/2026
 * Updated: 06/19/2026 - updated to standard logging format using StructuredArguments for json
 * Version: 1.1
 */

package org.bsr.springboot.practicebasicauth.security;

import jakarta.servlet.http.HttpServletRequest;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuthenticationEventLogger {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationEventLogger.class);
    private static final String APP_ID = "practice.basic-auth";
    private final HttpServletRequest request;

    @Autowired
    public AuthenticationEventLogger(HttpServletRequest request) {
        this.request = request;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();

        log.info("authn_login_success",
                StructuredArguments.keyValue("datetime", Instant.now().toString()),
                StructuredArguments.keyValue("appid", APP_ID),
                StructuredArguments.keyValue("event", "authn_login_success:" + username),
                StructuredArguments.keyValue("level", "INFO"),
                StructuredArguments.keyValue("description", "User " + username + " login successfully"),
                StructuredArguments.keyValue("source_ip", request.getRemoteAddr()),
                StructuredArguments.keyValue("uri", request.getRequestURI()),
                StructuredArguments.keyValue("method", request.getMethod())
        );

    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {

        String username = event.getAuthentication().getName();
        String reason = event.getException().getClass().getSimpleName();

        log.warn("authn_login_fail",
                StructuredArguments.keyValue("datetime", Instant.now().toString()),
                StructuredArguments.keyValue("appid", APP_ID),
                StructuredArguments.keyValue("event","authn_login_fail" + username),
                StructuredArguments.keyValue("level","WARN"),
                StructuredArguments.keyValue("description", "Login failure for user: " + username),
                StructuredArguments.keyValue("failure reason", reason),
                StructuredArguments.keyValue("source_ip", request.getRemoteAddr()),
                StructuredArguments.keyValue("uri", request.getRequestURI()),
                StructuredArguments.keyValue("method", request.getMethod()));
    }
}

/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Logs success and failure authentication events.
 * Created: 06/12/2026
 * Version: 1.0
 */

package org.bsr.springboot.practicebasicauth.security;

import jakarta.servlet.http.HttpServletRequest;
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
    private final HttpServletRequest request;

    @Autowired
    public AuthenticationEventLogger(HttpServletRequest request) {
        this.request = request;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        var authentication = event.getAuthentication();
        String timestamp = Instant.now().toString();

        log.info("[Authentication Event - SUCCESS] username:{}, ip:{}, timestamp:{}",
                authentication.getName(), request.getRemoteAddr(), timestamp);

    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        var authentication = event.getAuthentication();
        String ex = event.getException().getMessage();
        String timestamp = Instant.now().toString();

        log.warn("[Authentication Event - FAILURE] username:{}, ip:{}, message:{}, timestamp:{}",
                authentication.getName(), request.getRemoteAddr(), ex, timestamp);
    }
}

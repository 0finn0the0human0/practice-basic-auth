/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Logs success and failure authentication events. Follows standardized logging format from OWASP Application
 *          Logging Vocabulary Cheat Sheet.
 * Created: 06/12/2026
 * Version: 1.1
 */

package org.bsr.springboot.practicebasicauth.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventLogger {

    private final SecurityAuditLoggerUtil auditLogger;
    private final HttpServletRequest request;

    @Autowired
    public AuthenticationEventLogger(HttpServletRequest request, SecurityAuditLoggerUtil auditLogger) {
        this.request = request;
        this.auditLogger = auditLogger;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        auditLogger.logAuthSuccess(username, details.getRemoteAddress(), request.getMethod(), request.getRequestURI());

    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        String username = event.getAuthentication().getName();
        String reason = event.getException().getClass().getSimpleName();

        auditLogger.logAuthFailure(username, reason, details.getRemoteAddress(), request.getMethod(),
                request.getRequestURI());


    }
}

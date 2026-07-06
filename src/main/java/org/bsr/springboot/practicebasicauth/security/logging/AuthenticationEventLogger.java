/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Logs success and failure authentication events. Follows standardized logging format from OWASP Application
 *          Logging Vocabulary Cheat Sheet.
 * Created: 06/12/2026
 * Version: 1.1
 */

package org.bsr.springboot.practicebasicauth.security.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthenticationEventLogger {

    private final SecurityAuditLoggerUtil auditLogger;

    @Autowired
    public AuthenticationEventLogger(SecurityAuditLoggerUtil auditLogger) {
        this.auditLogger = auditLogger;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String username = event.getAuthentication().getName();
        WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();

        String method = attrs != null ? attrs.getRequest().getMethod() : "unknown";
        String requestedUri = attrs != null ? attrs.getRequest().getRequestURI() : "unknown";
        String ip = details != null ? details.getRemoteAddress() : "unknown";
        auditLogger.logAuthnSuccess(username, ip, method, requestedUri);

    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        String username = event.getAuthentication().getName();
        String reason = event.getException().getClass().getSimpleName();
        String method = attrs != null ? attrs.getRequest().getMethod() : "unknown";
        String requestedUri = attrs != null ? attrs.getRequest().getRequestURI() : "unknown";
        String ip = details != null ? details.getRemoteAddress() : "unknown";

        auditLogger.logAuthnFailure(username, reason, ip, method, requestedUri);


    }
}

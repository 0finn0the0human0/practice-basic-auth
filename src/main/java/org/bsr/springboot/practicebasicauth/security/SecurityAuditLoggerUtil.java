/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    A logging utility class to centralize structured logging code for Security Events such as authentication
 *          success, failure, unauthorized, and forbidden auth attempts. Follows standardized logging format from OWASP
 *          Application Logging Vocabulary Cheat Sheet.
 * Created: 06/23/2026
 * Version: 1.0
 */

package org.bsr.springboot.practicebasicauth.security;

import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SecurityAuditLoggerUtil {


    private static final String APP_ID = "practice.basic-auth";
    private static final Logger log = LoggerFactory.getLogger(SecurityAuditLoggerUtil.class);


    public void logAuthnSuccess(String username, String ip, String method, String requestUri) {
        log.info("authn_login_success",
                StructuredArguments.keyValue("datetime", Instant.now().toString()),
                StructuredArguments.keyValue("appid", APP_ID),
                StructuredArguments.keyValue("event", "authn_login_success:" + username),
                StructuredArguments.keyValue("source_ip", ip),
                StructuredArguments.keyValue("uri", requestUri),
                StructuredArguments.keyValue("method", method),
                StructuredArguments.keyValue("description", "User " + username + " login successfully")

        );
    }

    public void logAuthnFailure(String username, String reason, String ip, String method, String requestUri) {
        log.warn("authn_login_fail",
            StructuredArguments.keyValue("datetime", Instant.now().toString()),
            StructuredArguments.keyValue("appid", APP_ID),
            StructuredArguments.keyValue("event","authn_login_fail:" + username),
            StructuredArguments.keyValue("failure_reason", reason),
            StructuredArguments.keyValue("source_ip", ip),
            StructuredArguments.keyValue("uri", requestUri),
            StructuredArguments.keyValue("method", method),
            StructuredArguments.keyValue("description", "Login failure for user: " + username)

        );
    }

    public void logAuthzFailure(String username, String reason, String ip, String method, String requestUri) {
        log.warn("authz_access_denied",
                StructuredArguments.keyValue("datetime", Instant.now().toString()),
                StructuredArguments.keyValue("appid", APP_ID),
                StructuredArguments.keyValue("event","authz_login_fail:" + username + "," + requestUri),
                StructuredArguments.keyValue("failure reason", reason),
                StructuredArguments.keyValue("source_ip", ip),
                StructuredArguments.keyValue("uri", requestUri),
                StructuredArguments.keyValue("method", method),
                StructuredArguments.keyValue("description", "User " + username + " attempted to access a resource without entitlement")
        );

    }
}

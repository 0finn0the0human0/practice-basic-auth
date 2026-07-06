/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    A logging utility class to centralize structured logging code for Security Events such as authentication
 *          success, failure, unauthorized, and forbidden auth attempts. Follows standardized logging format from OWASP
 *          Application Logging Vocabulary Cheat Sheet.
 * Created: 06/23/2026
 * Version: 1.1
 */

package org.bsr.springboot.practicebasicauth.security.logging;

import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SecurityAuditLoggerUtil {


    private static final String APP_ID = "practice.basic-auth";
    private static final Logger log = LoggerFactory.getLogger(SecurityAuditLoggerUtil.class);

    // Structured log key constants (S1192)
    private static final String KEY_DATETIME = "datetime";
    private static final String KEY_APPID = "appid";
    private static final String KEY_EVENT = "event";
    private static final String KEY_SOURCE_IP = "source_ip";
    private static final String KEY_URI = "uri";
    private static final String KEY_METHOD = "method";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_FAILURE_REASON = "failure_reason";

    public void logAuthnSuccess(String username, String ip, String method, String requestUri) {
        log.info("authn_login_success",
                StructuredArguments.keyValue(KEY_DATETIME, Instant.now().toString()),
                StructuredArguments.keyValue(KEY_APPID, APP_ID),
                StructuredArguments.keyValue(KEY_EVENT, "authn_login_success:" + username),
                StructuredArguments.keyValue(KEY_SOURCE_IP, ip),
                StructuredArguments.keyValue(KEY_URI, requestUri),
                StructuredArguments.keyValue(KEY_METHOD, method),
                StructuredArguments.keyValue(KEY_DESCRIPTION, "User " + username + " login successfully")
        );
    }

    public void logAuthnFailure(String username, String reason, String ip, String method, String requestUri) {
        log.warn("authn_login_fail",
                StructuredArguments.keyValue(KEY_DATETIME, Instant.now().toString()),
                StructuredArguments.keyValue(KEY_APPID, APP_ID),
                StructuredArguments.keyValue(KEY_EVENT, "authn_login_fail:" + username),
                StructuredArguments.keyValue(KEY_FAILURE_REASON, reason),
                StructuredArguments.keyValue(KEY_SOURCE_IP, ip),
                StructuredArguments.keyValue(KEY_URI, requestUri),
                StructuredArguments.keyValue(KEY_METHOD, method),
                StructuredArguments.keyValue(KEY_DESCRIPTION, "Login failure for user: " + username)
        );
    }

    public void logAuthzFailure(String username, String reason, String ip, String method, String requestUri) {
        log.warn("authz_access_denied",
                StructuredArguments.keyValue(KEY_DATETIME, Instant.now().toString()),
                StructuredArguments.keyValue(KEY_APPID, APP_ID),
                StructuredArguments.keyValue(KEY_EVENT, "authz_login_fail:" + username + "," + requestUri),
                StructuredArguments.keyValue(KEY_FAILURE_REASON, reason),
                StructuredArguments.keyValue(KEY_SOURCE_IP, ip),
                StructuredArguments.keyValue(KEY_URI, requestUri),
                StructuredArguments.keyValue(KEY_METHOD, method),
                StructuredArguments.keyValue(KEY_DESCRIPTION, "User " + username + " attempted to access a resource without entitlement")
        );

    }
}

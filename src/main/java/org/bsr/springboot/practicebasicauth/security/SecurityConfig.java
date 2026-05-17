/**
 * Author:  Benjamin Soto-Roberts
 * Desc:    Configuration for securing spring boot application through user authentication and role based authorization
 *          on privileged endpoints
 * Created: 05/16/2026
 * Version: 1.0
 * */

package org.bsr.springboot.practicebasicauth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Correct for REST
                .csrf(AbstractHttpConfigurer::disable) // Correct for stateless
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/me").authenticated() // All valid users have USER role
                                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Admin users must have ADMIN role
                                .anyRequest().denyAll()) // Deny all other requests
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

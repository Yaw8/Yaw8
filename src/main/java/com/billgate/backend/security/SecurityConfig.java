package com.billgate.backend.security;

// Spring configuration annotation.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Spring Security configuration classes.
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Allows us to insert our JWT filter before Spring's default auth filter.
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // Defines Spring Security rules for the backend.
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // Disable CSRF for REST API.
                //
                // Mobile apps use JWT tokens,
                // not browser form sessions.
                .csrf(csrf -> csrf.disable())

                // Define which endpoints are public/protected.
                .authorizeHttpRequests(auth -> auth

                        // Login/register must stay public.
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()

                        // Everything else requires authentication.
                        .anyRequest().authenticated()
                )

                // Add JWT filter before Spring's username/password filter.
                .addFilterBefore(
                        new JwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
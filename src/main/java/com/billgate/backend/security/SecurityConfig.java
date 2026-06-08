package com.billgate.backend.security;

// Spring configuration annotation.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Spring Security configuration classes.
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
                .csrf(csrf -> csrf.disable())

                // Disable browser-style username/password popup.
                .httpBasic(httpBasic -> httpBasic.disable())

                // Disable Spring's default login form.
                .formLogin(formLogin -> formLogin.disable())

                // Disable Spring's default logout route.
                .logout(logout -> logout.disable())

                // Make API stateless.
                // JWT token is checked on every request.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Define public and protected endpoints.
                .authorizeHttpRequests(auth -> auth

                        // Login/register stay public.
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()

                        // Everything else requires JWT authentication.
                        .anyRequest().authenticated()
                )

                // Run JWT filter before Spring's default auth filter.
                .addFilterBefore(
                        new JwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
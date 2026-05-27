package com.billgate.backend.security;

// Servlet filter chain imports.
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Spring security authentication classes.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

// Web filter base class.
// Ensures this filter runs once per request.
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

// This filter checks every incoming request.
//
// It looks for:
// Authorization: Bearer <token>
//
// Then it validates/extracts the JWT user info
// and tells Spring Security the request is authenticated.
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Read Authorization header from request.
        String authHeader =
                request.getHeader("Authorization");

        // If there is no Bearer token, continue without authentication.
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer " prefix to get raw JWT token.
        String token =
                authHeader.substring(7);

        try {
            // Extract user id from JWT token.
            Long userId =
                    JwtUtil.extractUserId(token);

            // Create Spring Security authentication object.
            //
            // Principal = userId
            // Credentials = null
            // Authorities = empty for now
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            Collections.emptyList()
                    );

            // Store authentication in Spring Security context.
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (Exception e) {

            // If token is invalid, clear authentication.
            SecurityContextHolder.clearContext();
        }

        // Continue request chain.
        filterChain.doFilter(request, response);
    }
}
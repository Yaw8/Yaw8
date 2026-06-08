package com.billgate.backend.security;

// Servlet filter chain imports.
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Spring security authentication classes.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

// Web authentication details.
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

// Web filter base class.
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

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

        // DEBUG:
        // Shows which request reached the filter.
        System.out.println(
                "REQUEST: " +
                        request.getMethod() +
                        " " +
                        request.getRequestURI()
        );

        // DEBUG:
        // Shows whether Flutter sent the JWT token.
        System.out.println(
                "AUTH HEADER: " + authHeader
        );

        // If there is no Bearer token,
        // continue without authentication.
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

            // DEBUG:
            // Confirms JWT was successfully decoded.
            System.out.println(
                    "JWT USER ID: " + userId
            );

            // Give authenticated user a basic role.
            List<SimpleGrantedAuthority> authorities =
                    List.of(
                            new SimpleGrantedAuthority("ROLE_USER")
                    );

            // Create Spring Security authentication object.
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            authorities
                    );

            // Attach request details.
            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
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
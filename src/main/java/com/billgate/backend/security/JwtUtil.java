package com.billgate.backend.security;

// JWT library imports.
import io.jsonwebtoken.Jwts;

// Java Date class used for token timestamps.
import java.util.Date;

public class JwtUtil {

    // Secret key used to sign JWT tokens.
    //
    // IMPORTANT:
    // In production this should be stored securely
    // in environment variables or configuration files.
    private static final String SECRET_KEY =
            "billgate_secret_key_for_learning_only_change_later";

    // Token expiration time.
    //
    // Current setting:
    // 24 hours
    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24;

    // Creates JWT token after successful login.
    //
    // Token stores:
    // - user email
    // - user id
    public static String generateToken(
            Long userId,
            String email
    ) {

        return Jwts.builder()

                // Main token subject.
                .setSubject(email)

                // Custom claim storing user id.
                .claim("userId", userId)

                // Token creation time.
                .setIssuedAt(new Date())

                // Token expiration time.
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION_TIME
                        )
                )

                // Signs token using secret key.
                .signWith(
                        io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes()
                        )
                )

                // Final JWT string.
                .compact();
    }

    // Extracts user id from JWT token.
    //
    // Backend uses this later to determine:
    // "Which logged-in user owns this request?"
    public static Long extractUserId(
            String token
    ) {

        return Jwts.parserBuilder()

                // Uses same secret key to validate token.
                .setSigningKey(
                        io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                                SECRET_KEY.getBytes()
                        )
                )

                // Builds JWT parser.
                .build()

                // Parses token.
                .parseClaimsJws(token)

                // Gets token body.
                .getBody()

                // Extracts custom userId claim.
                .get("userId", Long.class);
    }
}
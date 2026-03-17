package com.nextword.backend.shared.security;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;


import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKeyString;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSecretKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean isTokenValid(String token, String usernameFromDatabase) {
        final String usernameFromToken = extractUsername(token);
        return (usernameFromToken.equals(usernameFromDatabase)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expirationDate.before(new Date());
    }
}

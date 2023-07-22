package com.example.authservice.jwt;

import com.example.authservice.controller.AuthException;
import com.example.authservice.dtos.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JWTHelper {
    private static final String SECRET_KEY = "LSE1yJq4TJuIneLtE1ZjwkGITMQJrgiFFZr8vOTGCWc=";

    private static final SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), "HmacSHA256");

    public String generateToken(UserDto user) throws JsonProcessingException {

        var expirationDate = Date.from(Instant.now()
                .plus(1, ChronoUnit.DAYS));

        String issuer = "auth-service";
        String audience = "e-commerce-shop";

        Claims claims = Jwts.claims();
        claims.put("username", user.username());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .setAudience(audience)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Optional<String> decodeJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Optional.ofNullable(claims.get("username", String.class));
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
}

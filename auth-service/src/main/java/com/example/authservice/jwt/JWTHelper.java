package com.example.authservice.jwt;

import com.example.authservice.dtos.UserDto;
import com.example.authservice.dtos.UserHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
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

        var issuer = "auth-service";
        var audience = "e-commerce-store";

        var claims = Jwts.claims();
        claims.put("username", user.username());
        claims.put("userId", user.id());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .setAudience(audience)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Optional<UserHeader> decodeJWT(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            var userId = claims.get("userId", Long.class);
            var username = claims.get("username", String.class);

            var value = new UserHeader(userId, username);
            return Optional.of(value);
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
}

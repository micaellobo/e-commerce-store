package com.example.authservice.jwt;

import com.example.authservice.controller.AuthException;
import com.example.authservice.dtos.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTHelper {
    private static final String SECRET_KEY = "LSE1yJq4TJuIneLtE1ZjwkGITMQJrgiFFZr8vOTGCWc=";

    private static final SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), "HmacSHA256");

    public String generateToken(UserDto user) throws JsonProcessingException {
        Date expirationDate = new Date(System.currentTimeMillis() + (24 * 7 * 60 * 60 * 1000));
        String issuer = "auth-service";
        String audience = "e-commerce-shop";

        Claims claims = Jwts.claims();
        claims.put("username", user.username());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public String decodeJWT(String token) throws JsonProcessingException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("username", String.class);
        } catch (Exception e) {
            throw new AuthException(AuthException.GENERIC_LOGIN_FAIL);
        }
    }
}

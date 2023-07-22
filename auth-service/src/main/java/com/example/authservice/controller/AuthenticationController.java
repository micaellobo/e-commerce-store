package com.example.authservice.controller;

import com.example.authservice.dtos.LoginDto;
import com.example.authservice.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = {"api/v1/auth"})
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDto loginDto) {

        var jwt = authService.login(loginDto);

        var responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + jwt);

        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Object> validateToken(
            @RequestHeader("Authorization") String headerAuthorization) {

        log.info("POST - validateToken {}", headerAuthorization);

        var jwt = headerAuthorization.replace("Bearer ", "");

        var username = authService.validateToken(jwt);

        var responseHeaders = new HttpHeaders();
        responseHeaders.set("username", username);

        return ResponseEntity.ok().headers(responseHeaders).build();
    }
}

package com.example.authservice.controller;

import com.example.authservice.dtos.LoginDto;
import com.example.authservice.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.authservice.controller.AuthenticationController.CONTROLLER_PATH;

@Slf4j
@RestController
@RequestMapping(value = {CONTROLLER_PATH})
@RequiredArgsConstructor
public class AuthenticationController {

    public static final String CONTROLLER_PATH = "api/v1/auth";

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestHeader("CorrelationID") String correlationId,
            @Valid @RequestBody LoginDto loginDto) {

        log.info("POST - {}/login - {} - {}", CONTROLLER_PATH, correlationId, loginDto);

        var jwt = authService.login(loginDto);

        var responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + jwt);

        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Object> validateToken(
            @RequestHeader("CorrelationID") String correlationId,
            @RequestHeader("Authorization") String headerAuthorization) {
        log.info("POST - {}/validate - {} - {}", CONTROLLER_PATH, correlationId, headerAuthorization);

        var jwt = headerAuthorization.replace("Bearer ", "");

        var username = authService.validateToken(jwt);

        var responseHeaders = new HttpHeaders();
        responseHeaders.set("username", username);

        return ResponseEntity.ok().headers(responseHeaders).build();
    }
}

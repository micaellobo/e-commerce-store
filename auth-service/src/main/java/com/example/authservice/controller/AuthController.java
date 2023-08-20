package com.example.authservice.controller;

import com.example.authservice.dtos.LoginDto;
import com.example.authservice.config.*;
import com.example.authservice.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
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
public class AuthController {

    private final IAuthService authService;
    private final ContextHolder contextHolder;

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            HttpServletRequest request,
            @Valid @RequestBody LoginDto loginDto) {

        this.logRequest(request, loginDto);

        var jwt = this.authService.login(loginDto);

        var responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + jwt);

        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Object> validateToken(
            HttpServletRequest request,
            @RequestHeader("Authorization") String headerAuthorization) {

        this.logRequest(request, null);

        var jwt = headerAuthorization.replace("Bearer ", "");

        var userHeader = this.authService.validateToken(jwt);

        var responseHeaders = new HttpHeaders();
        responseHeaders.set("userId", userHeader.id().toString());
        responseHeaders.set("username", userHeader.username());

        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    private void logRequest(final HttpServletRequest request, final Object obj) {
        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), this.contextHolder.getCorrelationId(), this.contextHolder.getUsername(), obj);
    }
}

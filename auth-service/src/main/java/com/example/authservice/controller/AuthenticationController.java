package com.example.authservice.controller;

import com.example.authservice.dtos.LoginDto;
import com.example.authservice.dtos.UserDto;
import com.example.authservice.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/auth"})
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

    @GetMapping("/validate/{jwt}")
    public ResponseEntity<Object> validateToken(
            @PathVariable("jwt") String jwt) {

        var username = authService.validateToken(jwt);

        var responseHeaders = new HttpHeaders();
        responseHeaders.set("username", username);

        return ResponseEntity.ok().headers(responseHeaders).build();
    }
}

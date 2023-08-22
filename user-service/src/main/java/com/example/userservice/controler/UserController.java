package com.example.userservice.controler;

import com.example.userservice.config.ContextHolder;
import com.example.userservice.config.RequiresAuthentication;
import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = {"api/v1/users"})
public class UserController {

    private final IUserService userService;
    private final ContextHolder contextHolder;

    @PostMapping
    public ResponseEntity<Object> createUser(
            HttpServletRequest request,
            @Valid @RequestBody UserCreateDto userCreateDto) {

        this.logRequest(request, userCreateDto);

        var userDto = this.userService.addOne(userCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> getUserLogin(
            HttpServletRequest request,
            @RequestBody LoginDto loginDto) {

        this.logRequest(request, null);

        var userDto = this.userService.getUserLogin(loginDto);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/me")
    @RequiresAuthentication
    public ResponseEntity<Object> getUser(HttpServletRequest request) {

        this.logRequest(request, null);

        var userDto = this.userService.getUser();

        return ResponseEntity.ok(userDto);
    }

    private void logRequest(
            final HttpServletRequest request,
            final Object obj) {
        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), this.contextHolder.getCorrelationId(), this.contextHolder.getUsername(), obj);
    }
}

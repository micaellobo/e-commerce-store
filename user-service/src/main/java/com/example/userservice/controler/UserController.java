package com.example.userservice.controler;

import com.example.userservice.config.CustomContext;
import com.example.userservice.config.CustomContextHolder;
import com.example.userservice.config.RequiresAuthentication;
import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.IUserMapper;
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
    private final IUserMapper userMapper;
    private final CustomContextHolder contextHolder;

    @PostMapping
    public ResponseEntity<Object> createUser(
            HttpServletRequest request,
            @Valid @RequestBody UserCreateDto userCreateDto) {

        logRequest(request, userCreateDto);

        var user = userService.insertUser(userCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> getUserLogin(
            HttpServletRequest request,
            @RequestBody LoginDto loginDto) {

        logRequest(request, null);

        var user = userService.getUserLogin(loginDto);

        var dto = userMapper.toDto(user);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("me")
    @RequiresAuthentication
    public ResponseEntity<Object> getUser(HttpServletRequest request) {

        logRequest(request, null);

        var user = userService.getUser();

        var dto = userMapper.toDto(user);

        return ResponseEntity.ok(dto);
    }

    private void logRequest(final HttpServletRequest request, final Object obj) {
        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), contextHolder.getCorrelationId(), contextHolder.getUsername(), obj);
    }
}

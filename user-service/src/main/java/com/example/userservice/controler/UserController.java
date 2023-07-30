package com.example.userservice.controler;

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

    @GetMapping
    public ResponseEntity<Object> getUser(
            HttpServletRequest request,
            @RequestHeader("CorrelationID") String correlationId,
            @RequestHeader("username") String username) {

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, username, null);

        var user = userService.getUser(username);

        var dto = userMapper.toDto(user);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> getUserLogin(
            HttpServletRequest request,
            @RequestHeader String correlationId,
            @RequestBody LoginDto loginDto) {

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, null, loginDto);

        var user = userService.getUserLogin(loginDto);

        var dto = userMapper.toDto(user);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(
            HttpServletRequest request,
            @RequestHeader("CorrelationID") String correlationId,
            @Valid @RequestBody UserCreateDto userCreateDto) {

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, null, userCreateDto);

        var user = userService.insertUser(userCreateDto);

//        return ResponseEntity.created(URI.create("/" + user.getId())).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }
}

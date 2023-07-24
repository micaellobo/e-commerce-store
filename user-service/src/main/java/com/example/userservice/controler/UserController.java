package com.example.userservice.controler;

import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.IUserMapper;
import com.example.userservice.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.userservice.controler.UserController.CONTROLLER_PATH;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = {CONTROLLER_PATH})
public class UserController {

    public static final String CONTROLLER_PATH = "api/v1/users";

    private final IUserService userService;
    private final IUserMapper userMapper;

    @GetMapping
    public ResponseEntity<Object> getUser(
            @RequestHeader("CorrelationID") String correlationId,
            @RequestHeader("username") String username) {

        log.info("GET - {} - {} - {}", CONTROLLER_PATH, correlationId, username);

        var user = userService.getUser(username);

        var dto = userMapper.toDto(user);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> getUserLogin(
            @RequestHeader String correlationId,
            @RequestBody LoginDto loginDto) {

        log.info("GET - {}/login - {} - {}", CONTROLLER_PATH, correlationId, loginDto);

        var user = userService.getUserLogin(loginDto);

        var dto = userMapper.toDto(user);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(
            @RequestHeader("CorrelationID") String correlationId,
            @Valid @RequestBody UserCreateDto userCreateDto) {

        log.info("POST - {} - {} - {}", CONTROLLER_PATH, correlationId, userCreateDto);

        var user = userService.insertUser(userCreateDto);

//        return ResponseEntity.created(URI.create("/" + user.getId())).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }
}

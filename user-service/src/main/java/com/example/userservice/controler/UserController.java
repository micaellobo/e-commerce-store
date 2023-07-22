package com.example.userservice.controler;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.IUserMapper;
import com.example.userservice.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/users")
public class UserController {

    private final IUserService userService;
    private final IUserMapper userMapper;

    @GetMapping
    public ResponseEntity<Object> getUser(
            @RequestHeader("username") String username
    ) {
        log.info("GET - getUserById {}", username);
        var user = userService.getUser(username);

        var dto = userMapper.toDto(user);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(
            @Valid @RequestBody UserCreateDto userCreateDto
    ) {
        log.info("POST - createUser {}", userCreateDto);
        var user = userService.insertUser(userCreateDto);

//        return ResponseEntity.created(URI.create("/" + user.getId())).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }
}

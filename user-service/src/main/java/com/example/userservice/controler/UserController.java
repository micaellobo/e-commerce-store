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
@RequestMapping(path = "users")
public class UserController {

    private final IUserService userService;
    private final IUserMapper userMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(
            @PathVariable Long id
    ) {
        log.info("GET - getUserById {}", id);
        var user = userService.getUserById(id);

        var dto = userMapper.toDto(user);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(
            @Valid @RequestBody UserCreateDto userCreateDto
    ) {
        log.info("POST - createUser {}", userCreateDto);
        var user = userService.insertUser(userCreateDto);

//        return ResponseEntity.created(URI.create("/" + user.getId())).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }
}

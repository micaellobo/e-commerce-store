package com.example.userservice.controler;

import com.example.userservice.config.ContextHolder;
import com.example.userservice.config.RequiresAuthentication;
import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.service.IUserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = {"api/v1/users"})
@Tag(name = "User")
public class UserController {

    private final IUserService userService;
    private final ContextHolder contextHolder;

    /**
     * Create a new user
     *
     * @param request       The HttpServletRequest.
     * @param userCreateDto The user to create.
     * @return The created user.
     */
    @Operation(
            summary = "Create a new user",
            responses = {
                    @ApiResponse(responseCode = "201", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))}),
                    @ApiResponse(responseCode = "400", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))})
            }
    )
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            HttpServletRequest request,
            @Valid @RequestBody UserCreateDto userCreateDto) {

        this.logRequest(request, userCreateDto);

        var userDto = this.userService.addOne(userCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    /**
     * Get the user for login
     *
     * @param request  The HttpServletRequest.
     * @param loginDto The user to login.
     * @return The user.
     */
    @Hidden
    @PostMapping("/login")
    public ResponseEntity<UserDto> getUserLogin(
            HttpServletRequest request,
            @RequestBody LoginDto loginDto) {

        this.logRequest(request, null);

        var userDto = this.userService.getUserLogin(loginDto);

        return ResponseEntity.ok(userDto);
    }

    /**
     * Get the current user
     *
     * @param request The HttpServletRequest.
     * @return The user.
     */
    @Operation(
            summary = "Get the current user",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))}),
                    @ApiResponse(responseCode = "400", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))})
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/me")
    @RequiresAuthentication
    public ResponseEntity<UserDto> getUser(HttpServletRequest request) {

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

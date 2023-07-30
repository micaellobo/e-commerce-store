package com.example.authservice.service;

import com.example.authservice.controller.AuthException;
import com.example.authservice.dtos.LoginDto;
import com.example.authservice.dtos.UserDto;
import com.example.authservice.dtos.UserHeader;
import com.example.authservice.jwt.JWTHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final JWTHelper jwtHelper;
    private final RestTemplate restTemplate;

    @Value("${api.user-service}")
    private String userServiceUrl;

    @Override
    public String login(LoginDto loginDto) {

        var user = RequestUser(loginDto);

        try {
            return jwtHelper.generateToken(user);
        } catch (JsonProcessingException e) {
            throw new AuthException(AuthException.GENERIC_LOGIN_FAIL);
        }
    }

    private UserDto RequestUser(LoginDto loginDto) {
        var response = restTemplate.postForEntity(userServiceUrl + "/login", loginDto, UserDto.class);

        if (response.getStatusCode().isError())
            throw new AuthException(AuthException.GENERIC_LOGIN_FAIL);

        return response.getBody();
    }

    @Override
    public UserHeader validateToken(String jwt) {
        return jwtHelper.decodeJWT(jwt)
                .orElseThrow(() -> new AuthException(AuthException.GENERIC_LOGIN_FAIL));
    }
}

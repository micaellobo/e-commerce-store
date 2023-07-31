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
    private final IUserServiceClient userServiceClient;

    @Override
    public String login(final LoginDto loginDto) {

        var user = userServiceClient.getUserForLogin(loginDto)
                .orElseThrow(() -> new AuthException(AuthException.GENERIC_LOGIN_FAIL));

        try {
            return jwtHelper.generateToken(user);
        } catch (JsonProcessingException e) {
            throw new AuthException(AuthException.GENERIC_LOGIN_FAIL);
        }
    }


    @Override
    public UserHeader validateToken(final String jwt) {
        return jwtHelper.decodeJWT(jwt)
                .orElseThrow(() -> new AuthException(AuthException.GENERIC_LOGIN_FAIL));
    }
}

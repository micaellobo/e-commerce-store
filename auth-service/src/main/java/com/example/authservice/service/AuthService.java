package com.example.authservice.service;

import com.example.authservice.controller.AuthException;
import com.example.authservice.dtos.LoginDto;
import com.example.authservice.dtos.UserHeader;
import com.example.authservice.jwt.JWTHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final JWTHelper jwtHelper;
    private final IUserServiceClient userServiceClient;

    @Override
    public String login(final LoginDto loginDto) {

        var user = this.userServiceClient.getUserForLogin(loginDto)
                .orElseThrow(() -> new AuthException(AuthException.GENERIC_LOGIN_FAIL));

        try {
            return this.jwtHelper.generateToken(user);
        } catch (JsonProcessingException e) {
            throw new AuthException(AuthException.GENERIC_LOGIN_FAIL);
        }
    }


    @Override
    public UserHeader validateToken(final String jwt) {
        return this.jwtHelper.decodeJWT(jwt)
                .orElseThrow(() -> new AuthException(HttpStatus.UNAUTHORIZED, AuthException.GENERIC_LOGIN_FAIL));
    }
}

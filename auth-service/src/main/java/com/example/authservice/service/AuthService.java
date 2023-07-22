package com.example.authservice.service;

import com.example.authservice.config.HashUtils;
import com.example.authservice.controller.AuthException;
import com.example.authservice.dtos.IUserMapper;
import com.example.authservice.dtos.LoginDto;
import com.example.authservice.dtos.UserDto;
import com.example.authservice.jwt.JWTHelper;
import com.example.authservice.repository.IAuthRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {

    private final IAuthRepository authRepository;
    private final JWTHelper jwtHelper;
    private final IUserMapper userMapper;

    @Override
    public String login(LoginDto loginDto) {
        var user = authRepository.findUserByUsername(loginDto.username())
                .orElseThrow(() -> new AuthException(AuthException.NOT_FOUND));

        var input = HashUtils.Sha256Hash(loginDto.password());
        if (!input.equals(user.getPassword())) {
            throw new AuthException(AuthException.GENERIC_LOGIN_FAIL);
        }

        try {
            return jwtHelper.generateToken(userMapper.toDto(user));
        } catch (JsonProcessingException e) {
            throw new AuthException(AuthException.GENERIC_LOGIN_FAIL);
        }
    }

    @Override
    public String validateToken(String jwt) {
        return jwtHelper.decodeJWT(jwt)
                .orElseThrow(() -> new AuthException(AuthException.GENERIC_LOGIN_FAIL));
    }
}

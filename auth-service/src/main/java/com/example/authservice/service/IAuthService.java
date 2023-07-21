package com.example.authservice.service;


import com.example.authservice.dtos.LoginDto;
import com.example.authservice.dtos.UserDto;

public interface IAuthService {
    public String login(LoginDto loginDto);

    String validateToken(String jwt);
}

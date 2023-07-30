package com.example.authservice.service;


import com.example.authservice.dtos.LoginDto;
import com.example.authservice.dtos.UserHeader;

public interface IAuthService {
    public String login(LoginDto loginDto);

    UserHeader validateToken(String jwt);
}

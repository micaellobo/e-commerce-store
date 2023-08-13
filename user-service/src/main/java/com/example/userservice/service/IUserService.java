package com.example.userservice.service;

import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserEditDto;

public interface IUserService {
    UserDto addOne(UserCreateDto userCreateDto);

    UserDto getUser();

    UserDto editUser(UserEditDto userEditDto);

    UserDto getUserLogin(LoginDto loginDto);
}

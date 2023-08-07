package com.example.userservice.service;

import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.models.User;
import com.example.userservice.dto.UserEditDto;

public interface IUserService {
    User addOne(UserCreateDto userCreateDto);

    User getUser();

    User editUser(UserEditDto userEditDto);

    User getUserLogin(LoginDto loginDto);
}

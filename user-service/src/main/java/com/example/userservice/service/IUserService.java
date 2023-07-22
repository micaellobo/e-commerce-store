package com.example.userservice.service;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.models.User;
import com.example.userservice.dto.UserEditDto;

public interface IUserService {
    User insertUser(UserCreateDto userCreateDto);

    User getUser(String username);

    User editUser(UserEditDto userEditDto);
}

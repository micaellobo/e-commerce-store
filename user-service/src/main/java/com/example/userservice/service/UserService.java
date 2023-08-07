package com.example.userservice.service;

import com.example.userservice.config.CustomContextHolder;
import com.example.userservice.dto.*;
import com.example.userservice.utils.HashUtils;
import com.example.userservice.controler.UserException;
import com.example.userservice.models.User;
import com.example.userservice.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IUserMapper userMapper;
    private final CustomContextHolder contextHolder;

    @Override
    public User insertUser(final UserCreateDto userCreateDto) {

        var password = HashUtils.Sha256Hash(userCreateDto.password());

        var user = userMapper.toUser(userCreateDto, password);

        var exists = userRepository.existsUser(user);

        if (exists)
            throw new UserException(UserException.USER_ALREADY_EXISTS);

        return userRepository.save(user);
    }

    @Override
    public User getUser() {
        return userRepository.findByUsername(contextHolder.getUsername())
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
    }

    @Override
    public User getUserLogin(LoginDto loginDto) {
        var password = HashUtils.Sha256Hash(loginDto.password());

        return userRepository.findByUsernameAndPassword(loginDto.username(), password)
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));
    }

    @Override
    public User editUser(final UserEditDto userEditDto) {

        var user = userRepository.findById(userEditDto.id())
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));

        userMapper.partialUpdate(userEditDto, user);

        return userRepository.save(user);
    }


}

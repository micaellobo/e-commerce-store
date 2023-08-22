package com.example.userservice.service;

import com.example.userservice.config.ContextHolder;
import com.example.userservice.dto.*;
import com.example.userservice.utils.HashUtils;
import com.example.userservice.controler.UserException;
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
    private final ContextHolder contextHolder;

    @Override
    public UserDto addOne(final UserCreateDto userCreateDto) {

        var password = HashUtils.sha256Hash(userCreateDto.password());

        var user = this.userMapper.toUser(userCreateDto, password);

        var exists = this.userRepository.existsUser(user);

        if (exists)
            throw new UserException(UserException.USER_ALREADY_EXISTS);

        var saveSaved = this.userRepository.save(user);

        return this.userMapper.toDto(saveSaved);
    }

    @Override
    public UserDto getUser() {
        var userSaved = this.userRepository.findByUsername(this.contextHolder.getUsername())
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));

        return this.userMapper.toDto(userSaved);
    }

    @Override
    public UserDto getUserLogin(LoginDto loginDto) {
        var password = HashUtils.sha256Hash(loginDto.password());

        var user = this.userRepository.findByUsernameAndPassword(loginDto.username(), password)
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));

        return this.userMapper.toDto(user);
    }

    @Override
    public UserDto editUser(final UserEditDto userEditDto) {

        var user = this.userRepository.findById(userEditDto.id())
                .orElseThrow(() -> new UserException(UserException.USER_NOT_FOUND));

        this.userMapper.partialUpdate(userEditDto, user);

        var userEdited = this.userRepository.save(user);

        return this.userMapper.toDto(userEdited);
    }
}

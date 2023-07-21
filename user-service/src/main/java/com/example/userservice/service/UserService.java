package com.example.userservice.service;

import com.example.userservice.config.HashUtils;
import com.example.userservice.controler.UserException;
import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.IUserMapper;
import com.example.userservice.models.User;
import com.example.userservice.dto.UserEditDto;
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

    @Override
    public User insertUser(final UserCreateDto userCreateDto) {

        var user = userMapper.toUser(userCreateDto);

//        boolean exists = userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail());
        boolean exists = userRepository.existsUser(user);

        if (exists)
            throw new UserException(UserException.USER_ALREADY_EXISTS);

        user.setPassword(HashUtils.Sha256Hash(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User getUserById(final Long id) {
        return userRepository.findById(id)
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

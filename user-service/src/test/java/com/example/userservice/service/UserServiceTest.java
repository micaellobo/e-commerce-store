package com.example.userservice.service;

import com.example.userservice.config.CustomContextHolder;
import com.example.userservice.controler.UserException;
import com.example.userservice.dto.IUserMapper;
import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserEditDto;
import com.example.userservice.models.User;
import com.example.userservice.repository.IUserRepository;
import com.example.userservice.utils.HashUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    IUserRepository userRepository;

    @Mock
    private IUserMapper userMapper;

    @Mock
    CustomContextHolder contextHolder;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void beforeEach() {
        Mockito.lenient().when(contextHolder.getUserId()).thenReturn(1L);
        Mockito.lenient().when(contextHolder.getUsername()).thenReturn("testUser");
    }

    @Test
    void addOne_exists() {
        // Arrange
        var userCreateDto = UserCreateDto.builder()
                .name("John")
                .email("jhondoe@gmail.com")
                .username("jhondoe")
                .password("pwd")
                .build();

        when(userMapper.toUser(Mockito.eq(userCreateDto), Mockito.anyString())).thenReturn(new User());
        when(userRepository.existsUser(Mockito.any(User.class))).thenReturn(true);

        // Act and Assert
        Assertions.assertThrows(UserException.class,
                () -> userService.addOne(userCreateDto),
                UserException.USER_ALREADY_EXISTS);

        verify(userRepository, never()).save(Mockito.any(User.class));
    }

    @Test
    void addOne_success() {
        //Arrange
        var userCreateDto = UserCreateDto.builder()
                .name("John")
                .email("jhondoe@gmail.com")
                .username("jhondoe")
                .password(HashUtils.Sha256Hash("pwd"))
                .build();

        when(userMapper.toUser(Mockito.eq(userCreateDto), Mockito.anyString())).thenReturn(new User());
        when(userRepository.existsUser(Mockito.any(User.class))).thenReturn(false);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());

        //Act
        var addedUser = userService.addOne(userCreateDto);

        //Assert
        Assertions.assertNotNull(addedUser);
    }

    @Test
    void getUser() {
        // Arrange
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(
                UserException.class,
                userService::getUser,
                UserException.USER_ALREADY_EXISTS
        );
    }

    @Test
    void getUser_success() {
        // Arrange
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(new User()));

        //Act
        var user = userService.getUser();

        //Assert
        Assertions.assertNotNull(user);
    }

    @Test
    void testGetUserLogin_Success() {
        //Arrange
        var loginDto = LoginDto
                .builder()
                .username("john")
                .password("pwd")
                .build();

        when(userRepository.findByUsernameAndPassword(Mockito.anyString(), anyString())).thenReturn(Optional.of(new User()));

        // Act
        var user = userService.getUserLogin(loginDto);

        // Assert
        Assertions.assertNotNull(user);
    }


    @Test
    void testEditUser_Success() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Call the service method
        var userEditDto = new UserEditDto(1L, "jhon", "pwd");
        var editedUser = userService.editUser(userEditDto);

        // Assert the result
        Assertions.assertNotNull(editedUser);
    }

    @Test
    void testEditUser_Fail() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the service method
        var userEditDto = new UserEditDto(1L, "jhon", "pwd");

        // Act and Assert
        Assertions.assertThrows(
                UserException.class,
                () -> userService.editUser(userEditDto),
                UserException.USER_NOT_FOUND
        );
    }
}
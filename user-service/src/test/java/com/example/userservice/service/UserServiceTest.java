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
    private UserCreateDto userCreateDto;
    private User user;
    private LoginDto loginDto;
    private UserEditDto userEditDto;

    @BeforeEach
    void beforeEach() {
        Mockito.lenient().when(contextHolder.getUserId()).thenReturn(1L);
        Mockito.lenient().when(contextHolder.getUsername()).thenReturn("testUser");

        userCreateDto = UserCreateDto.builder()
                .name("John")
                .email("jhondoe@gmail.com")
                .username("jhondoe")
                .password("pwd")
                .build();


        user = User.builder()
                .email(userCreateDto.email())
                .name(userCreateDto.name())
                .password(HashUtils.Sha256Hash(userCreateDto.password()))
                .username(userCreateDto.username())
                .build();

        loginDto = LoginDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        userEditDto = UserEditDto.builder()
                .id(1L)
                .name("newName")
                .password("pwd")
                .build();
    }

    @Test
    void addOne_UserDoesNotExist_ShouldSaveUser() {
        //Arrange
        when(userMapper.toUser(userCreateDto, user.getPassword())).thenReturn(user);
        when(userRepository.existsUser(user)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        //Act
        var addedUser = userService.addOne(userCreateDto);

        //Assert
        Assertions.assertNotNull(addedUser);
    }

    @Test
    void addOne_UserAlreadyExists_ShouldThrowUserException() {
        // Arrange
        when(userMapper.toUser(userCreateDto, user.getPassword())).thenReturn(user);
        when(userRepository.existsUser(user)).thenReturn(true);

        // Act and Assert
        Assertions.assertThrows(UserException.class,
                () -> userService.addOne(userCreateDto),
                UserException.USER_ALREADY_EXISTS);

        verify(userRepository, never()).save(user);
    }

    @Test
    void getUser_UserDoesNotExist_ThrowsUserException() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(
                UserException.class,
                userService::getUser,
                UserException.USER_ALREADY_EXISTS
        );
    }

    @Test
    void getUser_UserExists_ReturnsUser() {
        // Arrange
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        //Act
        var userGet = userService.getUser();

        //Assert
        Assertions.assertNotNull(userGet);
    }

    @Test
    void getUserLogin_ValidLogin_ReturnsUser() {
        //Arrange
        when(userRepository.findByUsernameAndPassword(Mockito.anyString(), anyString())).thenReturn(Optional.of(user));

        // Act
        var userGet = userService.getUserLogin(loginDto);

        // Assert
        Assertions.assertNotNull(userGet);
    }

    @Test
    void getUserLogin_InvalidLogin_ThrowsUserException() {
        //Arrange
        when(userRepository.findByUsernameAndPassword(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());

        // Act

        // Act and Assert
        Assertions.assertThrows(
                UserException.class,
                () -> userService.getUserLogin(loginDto),
                UserException.USER_NOT_FOUND
        );
    }


    @Test
    void editUser_ValidUserEdit_ReturnsEditedUser() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(new User());

        doNothing().when(userMapper).partialUpdate(isA(UserEditDto.class), isA(User.class));

        // Call the service method
        var editedUser = userService.editUser(userEditDto);

        // Assert the result
        Assertions.assertNotNull(editedUser);
    }

    @Test
    void editUser_InvalidUserEdit_ThrowsUserException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(
                UserException.class,
                () -> userService.editUser(userEditDto),
                UserException.USER_NOT_FOUND
        );
    }
}
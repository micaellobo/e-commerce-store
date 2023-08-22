package com.example.userservice.service;

import com.example.userservice.config.ContextHolder;
import com.example.userservice.controler.UserException;
import com.example.userservice.dto.*;
import com.example.userservice.models.User;
import com.example.userservice.repository.IUserRepository;
import com.example.userservice.utils.HashUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    ContextHolder contextHolder;
    @InjectMocks
    UserService userService;
    private UserCreateDto userCreateDto;
    private User user;
    private UserDto userDto;
    private LoginDto loginDto;
    private UserEditDto userEditDto;

    @BeforeEach
    void beforeEach() {
        this.userCreateDto = UserCreateDto.builder()
                .name("John")
                .email("jhondoe@gmail.com")
                .username("jhondoe")
                .password("pwd")
                .build();

        this.user = User.builder()
                .email(this.userCreateDto.email())
                .name(this.userCreateDto.name())
                .password(HashUtils.sha256Hash(this.userCreateDto.password()))
                .username(this.userCreateDto.username())
                .build();

        this.userDto = UserDto.builder()
                .email(this.user.getEmail())
                .name(this.user.getName())
                .username(this.user.getUsername())
                .build();

        this.loginDto = LoginDto.builder()
                .username(this.user.getUsername())
                .password(this.user.getPassword())
                .build();

        this.userEditDto = UserEditDto.builder()
                .id(1L)
                .name("newName")
                .password("pwd")
                .build();

        lenient().when(this.contextHolder.getUserId())
                .thenReturn(1L);
        lenient().when(this.contextHolder.getUsername())
                .thenReturn(this.userCreateDto.username());
    }

    @Test
    void addOne_WhenUserDoesNotExist_ShouldSaveUser() {
        //Arrange
        when(this.userMapper.toUser(this.userCreateDto, this.user.getPassword()))
                .thenReturn(this.user);
        when(this.userRepository.existsUser(this.user))
                .thenReturn(false);
        when(this.userRepository.save(this.user))
                .thenReturn(this.user);
        when(this.userMapper.toDto(this.user))
                .thenReturn(this.userDto);

        //Act
        var addedUser = this.userService.addOne(this.userCreateDto);

        //Assert
        Assertions.assertEquals(this.userDto, addedUser);
    }

    @Test
    void addOne_WhenUserAlreadyExists_ShouldThrowUserException() {
        // Arrange
        when(this.userMapper.toUser(this.userCreateDto, this.user.getPassword()))
                .thenReturn(this.user);
        when(this.userRepository.existsUser(this.user))
                .thenReturn(true);

        // Act and Assert
        Assertions.assertThrows(UserException.class,
                () -> this.userService.addOne(this.userCreateDto),
                UserException.USER_ALREADY_EXISTS);

        verify(this.userRepository, never()).save(this.user);
    }

    @Test
    void getUser_WhenUserDoesNotExist_ShouldThrowUserException() {
        // Arrange
        when(this.userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(
                UserException.class,
                this.userService::getUser,
                UserException.USER_ALREADY_EXISTS
        );
    }

    @Test
    void getUser_WhenUserExists_ShouldReturnsUser() {
        // Arrange
        when(this.userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(this.user));
        when(this.userMapper.toDto(any(User.class)))
                .thenReturn(this.userDto);

        //Act
        var userGet = this.userService.getUser();

        //Assert
        Assertions.assertEquals(this.userDto, userGet);
    }

    @Test
    void getUserLogin_WhenValidLogin_ShouldReturnsUser() {
        //Arrange
        when(this.userRepository.findByUsernameAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(this.user));
        when(this.userMapper.toDto(any(User.class)))
                .thenReturn(this.userDto);

        // Act
        var userGet = this.userService.getUserLogin(this.loginDto);

        // Assert
        Assertions.assertEquals(this.userDto, userGet);
    }

    @Test
    void getUserLogin_WhenInvalidLogin_ShouldThrowUserException() {
        //Arrange
        when(this.userRepository.findByUsernameAndPassword(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(
                UserException.class,
                () -> this.userService.getUserLogin(this.loginDto),
                UserException.USER_NOT_FOUND
        );
    }

    @Test
    void editUser_WhenValidUserEdit_ShouldReturnsEditedUser() {
        //Arrange
        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User()));
        when(this.userRepository.save(any(User.class)))
                .thenReturn(new User());
        doNothing().when(this.userMapper).partialUpdate(isA(UserEditDto.class), isA(User.class));
        when(this.userMapper.toDto(any(User.class)))
                .thenReturn(this.userDto);

        // Act
        var editedUser = this.userService.editUser(this.userEditDto);

        // Assert
        Assertions.assertEquals(this.userDto, editedUser);
    }

    @Test
    void editUser_WhenInvalidUserEdit_ShouldThrowsUserException() {
        //Arrange
        when(this.userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(
                UserException.class,
                () -> this.userService.editUser(this.userEditDto),
                UserException.USER_NOT_FOUND
        );
    }
}
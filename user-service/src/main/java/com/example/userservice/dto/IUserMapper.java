package com.example.userservice.dto;

import com.example.userservice.models.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserMapper {
    User toUser(UserDto userDto);

    @Mapping(target = "password", expression = "java(password)")
    User toUser(UserCreateDto userCreateDto, @Context String password);

    User toUser(UserEditDto userEditDto);

    UserDto toDto(User user);

    void partialUpdate(UserDto userDto, @MappingTarget User user);

    void partialUpdate(UserCreateDto userCreateDto, @MappingTarget User user);

    void partialUpdate(UserEditDto userEditDto, @MappingTarget User user);
}
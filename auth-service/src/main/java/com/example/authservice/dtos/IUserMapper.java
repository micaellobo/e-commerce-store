package com.example.authservice.dtos;

import com.example.authservice.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserMapper {
    User toUser(UserDto userDto);

    UserDto toDto(User user);

    void partialUpdate(UserDto userDto, @MappingTarget User user);

}
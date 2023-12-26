package com.javaintensive.telegrambot.mapper;

import com.javaintensive.telegrambot.dto.UserDto;
import com.javaintensive.telegrambot.model.userservice.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromDto(UserDto userDto);
    UserDto toDto(User user);
}

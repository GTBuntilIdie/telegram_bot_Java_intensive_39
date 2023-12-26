package com.javaintensive.telegrambot.dto;

import com.javaintensive.telegrambot.model.userservice.Role;
import lombok.Data;

@Data
public class UserDto {
    private long chatId;
    private String name;
    private Role role;
}

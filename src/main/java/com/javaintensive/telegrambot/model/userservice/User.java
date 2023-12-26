package com.javaintensive.telegrambot.model.userservice;

import com.javaintensive.telegrambot.model.userservice.Role;

public record User(long chatId, String name, Role role) {
}

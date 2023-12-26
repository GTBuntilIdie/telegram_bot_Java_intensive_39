package com.javaintensive.telegrambot.service;

import com.javaintensive.telegrambot.model.userservice.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    Map<Long, User> users = new HashMap<>();

    public Optional<User> findById(Long chatId) {
        return Optional.ofNullable(users.get(chatId));
    }

    public void save(User user) {
        users.put(user.chatId(), user);
    }

}

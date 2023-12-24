package com.javaintensive.telegrambot.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
    private final Long chatId;
    private final String name;
    private final Role role;
    private Bucket bucket;
}

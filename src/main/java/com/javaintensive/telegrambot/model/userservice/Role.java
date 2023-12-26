package com.javaintensive.telegrambot.model.userservice;

public enum Role {
    COURIER("Курьер"),
    CUSTOMER("Покупатель");

    private String title;

    Role(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}

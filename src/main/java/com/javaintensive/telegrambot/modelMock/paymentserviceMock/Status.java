package com.javaintensive.telegrambot.modelMock.paymentserviceMock;

public enum Status {
    PAID("Оплачен"),
    NOT_PAID("Отменен");

    private String title;

    Status(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}

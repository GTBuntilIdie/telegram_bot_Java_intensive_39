package com.javaintensive.telegrambot.modelMock.paymentserviceMock.payment;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PAID("Оплачено"),
    NOT_PAID("Отменено");

    String title;

    PaymentStatus(String title) {
        this.title = title;
    }
}

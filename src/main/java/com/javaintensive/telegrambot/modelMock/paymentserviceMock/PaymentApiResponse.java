package com.javaintensive.telegrambot.modelMock.paymentserviceMock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentApiResponse {
    private PaymentStatus status;
    private Long orderId;
    private String date;
    private Integer price;
}

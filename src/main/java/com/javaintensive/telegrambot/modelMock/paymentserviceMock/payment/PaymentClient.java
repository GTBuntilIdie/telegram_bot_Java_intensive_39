package com.javaintensive.telegrambot.modelMock.paymentserviceMock.payment;

import com.javaintensive.telegrambot.modelMock.paymentserviceMock.payment.PaymentApiResponse;
import com.javaintensive.telegrambot.modelMock.paymentserviceMock.payment.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "payment", url = "http://localhost:9090")
public interface PaymentClient {

    @PostMapping(path = "/payment", consumes = "application/json",
            produces = "application/json")
    PaymentApiResponse doPayment(@RequestBody PaymentDTO paymentDto);

    @GetMapping(path = "/payment/{orderId}", consumes = "application/json",
            produces = "application/json")
    PaymentApiResponse checkPayment(@PathVariable("orderId") Long orderId);
}



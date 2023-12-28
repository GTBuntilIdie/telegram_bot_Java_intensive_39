package com.javaintensive.telegrambot.modelMock.paymentserviceMock;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "payment-service", url = "http://localhost:9090")
public interface PaymentClient {

    /*@GetMapping(path = "/payment/{orderId}")
    ResponseEntity<PaymentApiResponse> checkPayment(@PathVariable Long orderId);*/

    @PostMapping(path = "/payment", consumes = "application/json",
            produces = "application/json")
    PaymentApiResponse doPayment(@RequestBody PaymentDTO paymentDto);

    @GetMapping(path = "/payment/{orderId}", consumes = "application/json",
            produces = "application/json")
    PaymentApiResponse checkPayment(@PathVariable("orderId") Long orderId);
}



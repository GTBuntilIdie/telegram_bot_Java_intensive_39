package com.javaintensive.telegrambot.modelMock.paymentserviceMock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class Controller {

    private final PaymentClient externalPaymentApi;

    @Autowired
    public Controller(PaymentClient externalPaymentApi) {
        this.externalPaymentApi = externalPaymentApi;
    }


    @PostMapping("/payment")
   PaymentApiResponse paymentApiResponce(@RequestBody PaymentDTO dto) {
        return externalPaymentApi.doPayment(dto);
    }

    @GetMapping(path = "/payment/{orderId}", consumes = "application/json",
            produces = "application/json")
    public PaymentApiResponse checkPayment(@PathVariable Long orderId) {
        System.out.println(orderId);

        return externalPaymentApi.checkPayment(orderId); }


}

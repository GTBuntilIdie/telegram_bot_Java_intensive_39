package com.javaintensive.telegrambot.modelMock.paymentserviceMock.payment;

import com.javaintensive.telegrambot.modelMock.paymentserviceMock.PaymentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentClient externalPaymentApi;

    @Autowired
    public PaymentService(PaymentClient externalPaymentApi) {
        this.externalPaymentApi = externalPaymentApi;
    }


    public PaymentDTO createPaymentDto() {
        // Здесь можно выполнить логику для создания и заполнения DTO

        return new PaymentDTO();
    }

    public PaymentApiResponse pay(PaymentData paymentInfo, BigDecimal sum) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderId(paymentInfo.getOrderId());
        paymentDTO.setCardNumber(paymentInfo.getCardNumber());
        paymentDTO.setCustomerName(paymentInfo.getCardholderName());
        paymentDTO.setDateCard(paymentInfo.getDateCard());
        paymentDTO.setCvv(paymentInfo.getCvv());
        paymentDTO.setPrice(sum.intValue());
        return externalPaymentApi.doPayment(paymentDTO);
    }
}



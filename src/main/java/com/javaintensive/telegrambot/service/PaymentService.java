package com.javaintensive.telegrambot.service;

import com.javaintensive.telegrambot.model.paymentservice.PaymentData;
import com.javaintensive.telegrambot.model.paymentservice.Check;
import com.javaintensive.telegrambot.model.paymentservice.Status;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    public Check pay(PaymentData paymentInfo, BigDecimal sum) {
        Check paymentStatus = new Check();
        paymentStatus.setStatus(Status.PAID);
        paymentStatus.setSum(sum);
        paymentStatus.setDate(LocalDateTime.now());
        paymentStatus.setOrderId(paymentInfo.getOrderId());
        return paymentStatus;
    }
}

package com.javaintensive.telegrambot.servicemock;

import com.javaintensive.telegrambot.modelMock.paymentserviceMock.PaymentData;
import com.javaintensive.telegrambot.modelMock.paymentserviceMock.Check;
import com.javaintensive.telegrambot.modelMock.paymentserviceMock.Status;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceMock {

    public Check pay(PaymentData paymentInfo, BigDecimal sum) {
        Check check = new Check();
        check.setStatus(Status.PAID);
        check.setSum(sum);
        check.setDate(LocalDateTime.now());
        check.setOrderId(paymentInfo.getOrderId());
        return check;
    }

}

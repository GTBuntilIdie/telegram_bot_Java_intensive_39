package com.javaintensive.telegrambot.modelMock.paymentserviceMock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Check {
    private Status status;
    private Long orderId;
    private LocalDateTime date;
    private BigDecimal sum;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}

package com.javaintensive.telegrambot.modelMock;

import com.javaintensive.telegrambot.modelMock.orderserviceMock.Order;
import com.javaintensive.telegrambot.modelMock.orderserviceMock.OrderPreparedToPay;
import com.javaintensive.telegrambot.modelMock.paymentserviceMock.PaymentData;

public class UserActionsDataMock {

    private Bucket bucket;

    private Order order;

    private OrderPreparedToPay orderPreparedToPay;

    private PaymentData paymentInfo;

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderPreparedToPay getOrderPreparedToPay() {
        return orderPreparedToPay;
    }

    public void setOrderPreparedToPay(OrderPreparedToPay orderPreparedToPay) {
        this.orderPreparedToPay = orderPreparedToPay;
    }

    public PaymentData getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentData paymentInfo) {
        this.paymentInfo = paymentInfo;
    }
}

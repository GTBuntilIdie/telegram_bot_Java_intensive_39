package com.javaintensive.telegrambot.model;

import com.javaintensive.telegrambot.model.orderservice.Order;
import com.javaintensive.telegrambot.model.orderservice.OrderPreparedToPay;
import com.javaintensive.telegrambot.model.paymentservice.PaymentData;

public class UserActionsData {

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

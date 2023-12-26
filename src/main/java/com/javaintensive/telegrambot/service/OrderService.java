package com.javaintensive.telegrambot.service;

import com.javaintensive.telegrambot.model.orderservice.Order;
import com.javaintensive.telegrambot.model.orderservice.OrderPreparedToPay;
import com.javaintensive.telegrambot.model.storeservice.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {
    public OrderPreparedToPay sendOrder(Order order) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Product product : order.getProducts()) {
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())));
        }
        OrderPreparedToPay orderPreparedToPay = new OrderPreparedToPay(1L, sum);
        return orderPreparedToPay;
    }

    public void closeOrder(Order orderWithPaidStatusTrue) {

    }
}

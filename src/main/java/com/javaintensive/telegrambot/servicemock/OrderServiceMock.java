package com.javaintensive.telegrambot.servicemock;

import com.javaintensive.telegrambot.modelMock.orderserviceMock.Order;
import com.javaintensive.telegrambot.modelMock.orderserviceMock.OrderPreparedToPay;
import com.javaintensive.telegrambot.modelMock.storeserviceMock.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderServiceMock {
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

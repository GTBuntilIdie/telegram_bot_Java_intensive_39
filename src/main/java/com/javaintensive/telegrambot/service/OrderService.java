package com.javaintensive.telegrambot.service;


import com.javaintensive.telegrambot.dto.OrderGoodsDto;
import com.javaintensive.telegrambot.dto.OrderPreparedToPay;
import com.javaintensive.telegrambot.feignclient.OrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderClient orderClient;

    public OrderPreparedToPay prepareToPayOrder(OrderGoodsDto orderGoodsDto){
        OrderPreparedToPay orderPreparedToPay = orderClient.prepareToPayOrder(orderGoodsDto);
        return orderPreparedToPay;
    }

    public void setOrderStatusPaid(Long orderId){
        orderClient.setOrderStatusPaid(orderId);
    }
}

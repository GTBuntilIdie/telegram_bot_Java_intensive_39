package com.javaintensive.telegrambot.feignclient;

import com.javaintensive.telegrambot.dto.OrderGoodsDto;
import com.javaintensive.telegrambot.dto.OrderPreparedToPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "order-service", url = "${order-service.url}")
public interface OrderClient {

    @PostMapping("/prepareOrder")
    OrderPreparedToPay prepareToPayOrder(@RequestBody OrderGoodsDto orderGoodsDto);


//    @RequestMapping(method = RequestMethod.PATCH, value = "/order/{orderId}")
    @PatchMapping("/order/{orderId}")
    void setOrderStatusPaid(@PathVariable Long orderId);

}

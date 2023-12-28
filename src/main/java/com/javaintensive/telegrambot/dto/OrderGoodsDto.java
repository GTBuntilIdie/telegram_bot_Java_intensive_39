package com.javaintensive.telegrambot.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderGoodsDto {

    private Long storeId;
    private List<ProductDtoWithQuantity> productDtoWithQuantityList;
    private String user_address;
    private Long userId;
}

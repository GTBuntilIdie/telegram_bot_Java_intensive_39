package com.javaintensive.telegrambot.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderDto {

    private Long storeId;
    private Map<ProductDto, Integer> bucket;
    private String userAddress;
}

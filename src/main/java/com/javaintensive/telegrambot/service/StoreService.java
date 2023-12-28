package com.javaintensive.telegrambot.service;

import com.javaintensive.telegrambot.dto.ProductDto;
import com.javaintensive.telegrambot.dto.StoreDto;
import com.javaintensive.telegrambot.feignclient.StoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreClient storeClient;

    public List<StoreDto> getAllStores() {
        List<StoreDto> storeDtoList = storeClient.getAllStores();

        return storeDtoList;
    }

    public List<ProductDto> getGoodsByStoreId(Long storeId){
        List<ProductDto> productDtoList = storeClient.getGoodsByStoreId(storeId);

        return productDtoList;
    }

}

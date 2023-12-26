package com.javaintensive.telegrambot.service;

import com.javaintensive.telegrambot.model.orderservice.Order;
import com.javaintensive.telegrambot.model.storeservice.Product;
import com.javaintensive.telegrambot.model.storeservice.Store;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreService {

    Map<Long, Store> stores = new HashMap<>();
    Map<Store, List<Product>> products = new HashMap<>();
    {
        stores.put(1L, new Store(1L, "Пятерочка", "Это Пятерочка", "Пушкина Дом Колотушкина", "888888"));
        stores.put(2L, new Store(2L,"Магнит", "Это Магнит", "Пушкина Дом Колотушкина", "888888" ));
        stores.put(3L, new Store(3L,"Улыбка", "Это Улыбка", "Пушкина Дом Колотушкина", "888888" ));
        for(Store store : stores.values()) {
            products.put(store, List.of(
                    new Product(1L, "Квас", "Самый Вкусный квас", 5, new BigDecimal(20), store.id(), 0),
                    new Product(2L, "Кофе", "Совсем новый кофе", 50, new BigDecimal(40), store.id(), 0)));
        }
    }

    public List<Store> findAll() {
        return stores.values().stream().toList();
    }

    public Store findById(Long id) {
        return stores.get(id);
    }

    public List<Product> findGoodsByStore(Store store) {
        return products.get(store);
    }
}

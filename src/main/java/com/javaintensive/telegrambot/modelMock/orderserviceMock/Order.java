package com.javaintensive.telegrambot.modelMock.orderserviceMock;

import com.javaintensive.telegrambot.modelMock.storeserviceMock.Product;

import java.util.List;

public class Order {
    private Long storeId;
    private Long userId;
    private List<Product> products;
    private String address;
    private boolean paid;

    public Order(Long storeId, Long chatId, List<Product> products, String address) {
        this.storeId = storeId;
        this.userId = chatId;
        this.products = products;
        this.address = address;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}

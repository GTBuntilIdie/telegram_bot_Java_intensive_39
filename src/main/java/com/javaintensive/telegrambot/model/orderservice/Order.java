package com.javaintensive.telegrambot.model.orderservice;

import com.javaintensive.telegrambot.model.storeservice.Product;

import java.util.List;

public class Order {
    private Long storeId;
    private Long chatId;
    private List<Product> products;
    private String address;
    private boolean paid;

    public Order(Long storeId, Long chatId, List<Product> products, String address) {
        this.storeId = storeId;
        this.chatId = chatId;
        this.products = products;
        this.address = address;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
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

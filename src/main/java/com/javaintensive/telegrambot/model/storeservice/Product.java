package com.javaintensive.telegrambot.model.storeservice;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private String description;
    private Integer limitQuantity;
    private BigDecimal price;
    private Long storeId;
    private Integer quantity;

    public Product(Long id, String name, String description, Integer limitQuantity, BigDecimal price, Long storeId, Integer quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.limitQuantity = limitQuantity;
        this.price = price;
        this.storeId = storeId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(Integer limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

package com.javaintensive.telegrambot.model.orderservice;

import java.math.BigDecimal;

public record OrderPreparedToPay(Long id, BigDecimal sum){}

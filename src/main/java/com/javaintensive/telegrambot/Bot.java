package com.javaintensive.telegrambot;

import com.javaintensive.telegrambot.model.Role;
import com.javaintensive.telegrambot.model.User;
import com.javaintensive.telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${telegram.bot.name}")
    private String name;

    private Map<Long, User> users = new HashMap<>();

    @Autowired
    private UserService userService;

    public Bot(@Value("${telegram.bot.token}") String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            switch (message.getText()) {
                case "/start" -> initUser(chatId);
                default -> messageError(chatId);
            }
        } else if(update.hasCallbackQuery()) {
            onClick(update.getCallbackQuery());
        }
    }

    private void onClick(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        switch (callbackQuery.getData()) {
            case "courier" -> courier(message);
            case "customer" -> customer(message);
            case "deliveries" -> getDeliveries(chatId);
            case "order" -> getStores(chatId);
            default -> messageError(chatId);
        }
    }

    private void initUser(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "Выберите роль");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton courier = new InlineKeyboardButton("Курьер");
        InlineKeyboardButton customer = new InlineKeyboardButton("Покупатель");
        courier.setCallbackData("courier");
        customer.setCallbackData("customer");
        row.add(courier);
        row.add(customer);
        inlineKeyboardMarkup.setKeyboard(List.of(row));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void customer(Message message) {
        long chatId = message.getChatId();
        boolean isCreated = createNewUser(message, chatId);
        if(isCreated) {
            SendMessage sendMessage = createSendMessage(chatId, "Нажмите кнопку чтобы сделать заказ");
            InlineKeyboardButton order = new InlineKeyboardButton("Сделать заказ");
            order.setCallbackData("order");
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(List.of(List.of(order)));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else alreadyRegistered(chatId);
    }

    private void courier(Message message) {
        long chatId = message.getChatId();
        boolean isCreated = createNewUser(message, chatId);
        if(isCreated) {
            SendMessage sendMessage = createSendMessage(chatId, "Нажмите кнопку чтобы посмотреть список заказов, которые нужно доставить");
            InlineKeyboardButton deliveries = new InlineKeyboardButton("Заказы");
            deliveries.setCallbackData("deliveries");
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(List.of(List.of(deliveries)));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else alreadyRegistered(chatId);
    }

    private void alreadyRegistered(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "Вы уже зарегистрированы");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean createNewUser(Message message, long chatId) {
        String name = message.getFrom().getUserName();
        if(!users.containsKey(chatId)) {
            User user = new User(chatId, name, Role.CUSTOMER);
            users.put(chatId, user);
            return true;
        }
        return false;
    }

    private void getDeliveries(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "На данный момент заказов нет");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void getStores(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "Магазины");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void messageError(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Unexpected Message");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public SendMessage createSendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Long.toString(chatId));
        sendMessage.setText(text);
        return sendMessage;
    }

    @Override
    public String getBotUsername() {
        return name;
    }
}
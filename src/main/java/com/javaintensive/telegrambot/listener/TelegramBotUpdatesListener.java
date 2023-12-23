package com.javaintensive.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.javaintensive.telegrambot.constants.Buttons.BUTTON_REGISTRATION;
import static com.javaintensive.telegrambot.constants.Buttons.BUTTON_REGISTRATION_CALLBACK_TEXT;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                Long chatId = update.message().chat().id();
                String messageText = update.message().text();
                System.out.println(messageText);
                String welcomeAnswer = "Для начала надо зарегаться";
                if (("/start").equals(messageText)) {
                    processStartCommand(update);

                } else {
                    sendMessage(chatId, "произошел елсе");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processStartCommand(Update update) {
        long chatId = update.message().chat().id();
        // тут мы берем чатайди пользователя и отправляем в
        // юзерсервис чтобы узнать зарегистрирован ли пользователь
        // Guest guest = guestRepository.findByChatId(chatId);
        if (true) {
            sendRegistrationMessage(chatId);
        }
    }

    private void sendRegistrationMessage(long chatId) {
        SendMessage message = new SendMessage(chatId, "Давай зарегистрируемся");
        // Adding buttons
        message.replyMarkup(createRegistrationButton());
        sendMessage(message);
    }

    private InlineKeyboardMarkup createRegistrationButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BUTTON_REGISTRATION).callbackData(BUTTON_REGISTRATION_CALLBACK_TEXT));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Заказать товар").callbackData("qwerty"));

        return inlineKeyboardMarkup;
    }

    private void sendMessage(long chatId, String messageText) {
        SendMessage message = new SendMessage(chatId, messageText);
        SendResponse response = telegramBot.execute(message);
    }
    private void sendMessage(SendMessage message) {
        SendResponse response = telegramBot.execute(message);
    }

    // метод дублирует нажатие кнопок сообщениями
    /*private void sendButtonClickMessage(long chatId, String message) {
        sendMessage(new SendMessage(chatId, message));
    }*/

    private void processButtonClick(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (callbackQuery != null) {
            long chatId = callbackQuery.message().chat().id();

            if (callbackQuery.data().equals(BUTTON_REGISTRATION_CALLBACK_TEXT)) {
                // Cat shelter selected
                /*sendButtonClickMessage(chatId, BUTTON_CAT_SHELTER_CALLBACK_TEXT);
                processCatShelterClick(chatId);*/

            } /*else if (callbackQuery.data().equals(BUTTON_DOG_SHELTER_CALLBACK_TEXT)) {

                sendButtonClickMessage(chatId, BUTTON_DOG_SHELTER_CALLBACK_TEXT);
                processDogShelterClick(chatId);
            }*/
        }
    }

}

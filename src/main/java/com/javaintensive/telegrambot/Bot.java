package com.javaintensive.telegrambot;

import com.javaintensive.telegrambot.model.UserActionsData;
import com.javaintensive.telegrambot.model.orderservice.Order;
import com.javaintensive.telegrambot.model.orderservice.OrderPreparedToPay;
import com.javaintensive.telegrambot.model.paymentservice.PaymentData;
import com.javaintensive.telegrambot.model.paymentservice.Check;
import com.javaintensive.telegrambot.model.storeservice.Product;
import com.javaintensive.telegrambot.model.userservice.Role;
import com.javaintensive.telegrambot.model.Bucket;
import com.javaintensive.telegrambot.model.storeservice.Store;
import com.javaintensive.telegrambot.model.userservice.User;
import com.javaintensive.telegrambot.service.OrderService;
import com.javaintensive.telegrambot.service.PaymentService;
import com.javaintensive.telegrambot.service.StoreService;
import com.javaintensive.telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

@Component
public class Bot extends TelegramLongPollingBot {

    private final static String HELP = """
            /start - регистрация.
            /back - вернуться в основное меню.
            /status - просмотр статуса.
            """;

    @Value("${telegram.bot.name}")
    private String name;

    private Map<Long, UserActionsData> userActionsDataMap = new HashMap<>();

    private Set<Long> enabledWriteAddress = new HashSet<>();

    private Set<Long> enabledWriteCardData = new HashSet<>();

    private Map<Long, Queue<String>> cardMessageQueues = new HashMap<>();

    @Autowired
    private CardValidator cardValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    public Bot(@Value("${telegram.bot.token}") String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            switch (message.getText()) {
                case "/start" -> start(chatId);
                case "/back" -> back(chatId);
                case "/help" -> help(chatId);
                case "/status" -> status(chatId);
                default -> {
                    if(enabledWriteAddress.contains(chatId))
                        readAddress(chatId, message.getText());
                    else if(enabledWriteCardData.contains(chatId)) {
                        readCardData(chatId, message.getText());
                    } else
                        messageError(chatId);
                }
            }
        } else if(update.hasCallbackQuery()) {
            onClick(update.getCallbackQuery());
        }
    }

    private void onClick(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        String data = callbackQuery.getData();
        switch (data) {
            case "courier" -> saveUser(message, Role.COURIER, this::orders);
            case "customer" -> saveUser(message, Role.CUSTOMER, this::stores);
            case "deliveries" -> deliveries(chatId);
            case "show bucket" -> showBucket(chatId);
            case "create order" -> writeAddress(chatId);
            case "pay" -> pay(chatId);
            default -> {
                if(data.matches("store: .*")) {
                    store(chatId, data);
                } else if(data.matches("good .*"))
                    bucketEvent(chatId, data);
                else
                    messageError(chatId);
            }
        }
    }

    private void start(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "Выберите роль");
        InlineKeyboardButton courier = createButton("Курьер", "courier");
        InlineKeyboardButton customer = createButton("Покупатель", "customer");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(List.of(List.of(courier, customer)));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        execute(sendMessage);
    }

    private void back(long chatId) {
        Optional<User> user = userService.findById(chatId);
        if(user.isPresent()) {
            Role role = user.get().role();
            switch (role) {
                case COURIER -> deliveries(chatId);
                case CUSTOMER -> {
                    userActionsDataMap.remove(chatId);
                    enabledWriteCardData.remove(chatId);
                    enabledWriteAddress.remove(chatId);
                    stores(chatId);
                }
            }
        }
        else
            notRegistered(chatId);
    }

    private void help(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, HELP);
        execute(sendMessage);
    }

    private void status(long chatId) {
        Optional<User> user = userService.findById(chatId);
        if(user.isEmpty())
            notRegistered(chatId);
        else {
            Role role = user.get().role();
            SendMessage sendMessage = createSendMessage(chatId, "Ваша роль: " + role.title());
            execute(sendMessage);
        }
    }

    private void saveUser(Message message, Role role, Consumer<Long> action) {
        long chatId = message.getChatId();
        Optional<User> optUser = userService.findById(chatId);
        if(optUser.isEmpty()) {
            User user = new User(chatId, message.getFrom().getUserName(), role);
            userService.save(user);
            action.accept(chatId);
        } else alreadyRegistered(chatId);
    }

    private void stores(long chatId) {
        List<Store> stores = storeService.findAll();
        SendMessage sendMessage = createSendMessage(chatId, "Выберите магазин");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for(Store store : stores) {
            InlineKeyboardButton button = createButton(store.name(), "store: " + store.id());
            buttons.add(List.of(button));
        }
        inlineKeyboardMarkup.setKeyboard(buttons);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        execute(sendMessage);
    }

    private void orders(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "Нажмите кнопку чтобы посмотреть список заказов, которые нужно доставить");
        InlineKeyboardButton deliveries = new InlineKeyboardButton("Заказы");
        deliveries.setCallbackData("deliveries");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(List.of(deliveries)));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        execute(sendMessage);
    }

    private void deliveries(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "На данный момент заказов нет");
        execute(sendMessage);
    }

    private void store(long chatId, String data) {
        Long storeId = Long.parseLong(data.replaceAll("store: ", ""));

        if(userActionsDataMap.containsKey(chatId)) {
            SendMessage sendMessage = createSendMessage(chatId, "Вы уже создали корзину, завершите заказ или отмените его, вызвав команду /back");
            execute(sendMessage);
        } else {
            Store store = storeService.findById(storeId);
            String storeDescription = createStoreDescription(store);
            List<Product> products = storeService.findGoodsByStore(store);
            UserActionsData buyingElements = new UserActionsData();
            buyingElements.setBucket(new Bucket(store.id(), products));
            this.userActionsDataMap.put(chatId, buyingElements);
            SendMessage storeDescriptionMessage = createSendMessage(chatId, storeDescription);
            execute(storeDescriptionMessage);


            for(Product product : products) {
                SendMessage productDescription = createSendMessage(chatId, createProductDescription(product));
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                InlineKeyboardButton add = createButton("+", "good add: " + product.getId());
                InlineKeyboardButton remove = createButton("-", "good remove: " + product.getId());
                inlineKeyboardMarkup.setKeyboard(List.of(List.of(add, remove)));
                productDescription.setReplyMarkup(inlineKeyboardMarkup);
                execute(productDescription);
            }

            InlineKeyboardButton showBucket = createButton("Посмотреть корзину", "show bucket");
            InlineKeyboardButton createOrder = createButton("Сформировать заказ", "create order");
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            SendMessage sendMessage = createSendMessage(chatId, "Чтобы посмотреть корзину нажмите \"Посмотреть корзину\"." +
                    "\nЧтобы сформировать заказ, нажмите \"Сформировать заказ\"");
            inlineKeyboardMarkup.setKeyboard(List.of(List.of(showBucket), List.of(createOrder)));
            storeDescriptionMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            execute(sendMessage);
        }
    }

    private void bucketEvent(long chatId, String data) {
        if(userActionsDataMap.containsKey(chatId)) {
            Bucket bucket = userActionsDataMap.get(chatId).getBucket();
            if(data.matches("good add: .*")) {
                Long productId = Long.parseLong(data.replaceAll("good add: ", ""));
                bucket.add(productId);
            } else {
                Long productId = Long.parseLong(data.replaceAll("good remove: ", ""));
                bucket.remove(productId);
            }
        } else
            bucketNotCreated(chatId);
    }

    private void showBucket(long chatId) {
        Bucket bucket = userActionsDataMap.get(chatId).getBucket();
        if(bucket == null)
            bucketNotCreated(chatId);
        else {
            StringBuilder products = new StringBuilder();
            for(Product product : bucket.getProducts()) {
                Integer quantity = product.getQuantity();
                if(product.getQuantity() > 0)
                    products.append(product.getName()).append(" - ").append(quantity).append("\n");
            }
            if(products.isEmpty())
                products.append("Корзина пуста");
            SendMessage sendMessage = createSendMessage(chatId, products.toString());
            execute(sendMessage);
        }
    }

    private void writeAddress(long chatId) {
        boolean isBucketEmpty = checkIsBucketEmpty(chatId);
        SendMessage sendMessage;
        if(isBucketEmpty) {
            sendMessage = createSendMessage(chatId, "Вы не выбрали ни одного товара");
        } else {
            sendMessage = createSendMessage(chatId, "Введите адрес доставки");
            enabledWriteAddress.add(chatId);
        }
        execute(sendMessage);
    }

    private boolean checkIsBucketEmpty(long chatId) {
        Collection<Product> products = userActionsDataMap.get(chatId).getBucket().getProducts();
        for(Product product : products) {
            if(product.getQuantity() > 0)
                return false;
        }
        return true;
    }

    private void alreadyRegistered(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "Вы уже зарегистрированы");
        execute(sendMessage);
    }

    private void messageError(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "Unexpected Message");
        execute(sendMessage);
    }

    private void notRegistered(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "Вам нужно зарегистрироваться");
        execute(sendMessage);
    }

    private void bucketNotCreated(long chatId) {
        SendMessage sendMessage = createSendMessage(chatId, "Корзина не создана. Выберите магазин");
        execute(sendMessage);
        stores(chatId);
    }
    
    private void readAddress(long chatId, String address) {
        enabledWriteAddress.remove(chatId);
        UserActionsData buyingElements = this.userActionsDataMap.get(chatId);
        Bucket bucket = this.userActionsDataMap.get(chatId).getBucket();
        List<Product> products = new ArrayList<>();
        for(Product product : bucket.getProducts()) {
            if(product.getQuantity() > 0) products.add(product);
        }
        Order order = new Order(bucket.getStoreId(), chatId, products, address);
        OrderPreparedToPay orderPreparedToPay = orderService.sendOrder(order);
        buyingElements.setOrder(order);
        buyingElements.setOrderPreparedToPay(orderPreparedToPay);
        SendMessage sendMessage = createSendMessage(chatId, "Заказ сформирован, итого: " +  orderPreparedToPay.sum().toString() + " руб.");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = createButton("Оплатить", "pay");
        inlineKeyboardMarkup.setKeyboard(List.of(List.of(button)));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        execute(sendMessage);
    }

    private void pay(long chatId) {
        enabledWriteCardData.add(chatId);
        Queue<String> cardMessageQueue = new ArrayDeque<>();
        cardMessageQueue.add("Введите имя держателя карты");
        cardMessageQueue.add("Введите срок действия карты");
        cardMessageQueue.add("Введите cvv");
        cardMessageQueues.put(chatId, cardMessageQueue);
        SendMessage sendMessage = createSendMessage(chatId, "Введите номер карты");
        PaymentData paymentInfo = new PaymentData();
        UserActionsData buyingElements = this.userActionsDataMap.get(chatId);
        buyingElements.setPaymentInfo(paymentInfo);
        execute(sendMessage);
    }

    private void readCardData(long chatId, String cardData) {
        cardData = cardData.strip();
        Queue<String> cardMessageQueue = cardMessageQueues.get(chatId);
        int messageCount = cardMessageQueue.size();
        UserActionsData buyingElements = this.userActionsDataMap.get(chatId);
        PaymentData paymentInfo = buyingElements.getPaymentInfo();
        String message;
        switch (messageCount) {
            case 3 -> {
                boolean isCorrect = cardValidator.isValidCardNumber(cardData);
                if(isCorrect) {
                    paymentInfo.setCardNumber(cardData);
                    message = cardMessageQueue.poll();
                } else
                    message = "Неверно введен номер карты. Номер карты состоит из 16 цифр";
                sendMessage(chatId, message);
            }
            case 2 -> {
                boolean isCorrect = cardValidator.isValidCardholderName(cardData);
                if(isCorrect) {
                    paymentInfo.setCardholderName(cardData);
                    message = cardMessageQueue.poll();
                } else
                    message = "Неверно введено имя держателя карты. Оно должно состоять из латинских букв и включать два слова";
                sendMessage(chatId, message);
            }
            case 1 -> {
                boolean isCorrect = cardValidator.isValidCardDate(cardData);
                if(isCorrect) {
                    paymentInfo.setDateCard(cardData);
                    message = cardMessageQueue.poll();
                } else
                    message = "Неверно введен срок действия карты. Верный формат: mm\\yy";
                sendMessage(chatId, message);
            }
            case 0 -> {
                boolean isCorrect = cardValidator.isValidCvv(cardData);
                if(isCorrect) {
                    enabledWriteCardData.remove(chatId);
                    cardMessageQueues.remove(chatId);
                    paymentInfo.setCvv(cardData);
                    message = "Ожидайте, пока произойдет оплата";
                    sendMessage(chatId, message);
                    sendToPaymentService(chatId);
                } else
                    sendMessage(chatId, "Неверно введен cvv код. Сvv код должен состоять из 3 цифр");
            }
        }
    }

    private void sendToPaymentService(long chatId) {
        UserActionsData userActionsData = userActionsDataMap.get(chatId);
        Order order = userActionsData.getOrder();
        order.setPaid(true);
        orderService.closeOrder(order);
        PaymentData paymentInfo = userActionsData.getPaymentInfo();
        BigDecimal sum = userActionsData.getOrderPreparedToPay().sum();
        Check paymentStatus = paymentService.pay(paymentInfo, sum);
        userActionsDataMap.remove(chatId);
        String check = "Чек:\n" +
                "Дата: " + paymentStatus.getDate() + "\n" +
                "Статус: " + paymentStatus.getStatus().title() + "\n" +
                "Итого: " + paymentStatus.getSum().toString() + " руб.";
        SendMessage sendMessage = createSendMessage(chatId, check);
        execute(sendMessage);
    }

    private void sendMessage(long chatId, String message) {
        SendMessage sendMessage = createSendMessage(chatId, message);
        execute(sendMessage);
    }

    private SendMessage createSendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Long.toString(chatId));
        sendMessage.setText(text);
        return sendMessage;
    }

    private InlineKeyboardButton createButton(String name, String callback) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(name);
        inlineKeyboardButton.setCallbackData(callback);
        return inlineKeyboardButton;
    }

    private String createStoreDescription(Store store) {
        return store.name().toUpperCase() + "\n" +
                "Описание:\n" + store.description() + "\n" +
                "Адрес: " + store.address() + "\n" +
                "Горячая линия магазина: " + store.phone();
    }

    private String createProductDescription(Product product) {
        return product.getName() + "\n" +
                product.getDescription() + "\n" +
                "Доступно на складе: " + product.getLimitQuantity() + "\n" +
                "Цена: " + product.getPrice() + " руб.";
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) {
        try {
            return super.execute(method);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }
}
package com.example.bot.application;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDate;


@Component
@PropertySource(value = "application.properties")
public class Main extends TelegramLongPollingBot {

    private static final String MESSAGE_1 = "\uD83E\uDD20 Message 1 ";
    private static final String MESSAGE_2 = "\uD83D\uDC7E Message 2 ";

    public Main() {
        System.out.println("DaysCounter constructor called");
    }

    @PostConstruct
    public void init() throws TelegramApiException {
        System.out.println("PostConstruct: botUsername = " + botName + ", botToken = " + token);
        if (botName == null || token == null || botName.isEmpty() || token.isEmpty()) {
            throw new IllegalStateException("Bot username or token is not set! Check application.properties");
        }

        // TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        // botsApi.registerBot(this);
        System.out.println("Bot initialized for webhook");
    }

    @Value("${telegrambot.username}")
    String botName;

    @Value("${telegrambot.token}")
    String token;

    private String chatId; // Зберігаємо chatId користувача

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Received update: " + update.toString());
        if (update.hasMessage()) {
            System.out.println("Update has message: " + update.getMessage());
            if (update.getMessage().hasText()) {
                chatId = update.getMessage().getChatId().toString();
                String userMessage = update.getMessage().getText();
                System.out.println("Message from chatId " + chatId + ": " + userMessage);
                switch (userMessage) {
                    case "/start":
                        sendMessage(chatId, "Вітаю!");
                        break;
                    case "/message1":
                        sendMessage1();
                        break;
                    case "/message2":
                        sendMessage2();
                        break;
                    default:
                        sendMessage(chatId, "Обери команду");
                }
            } else {
                System.out.println("Message has no text");
            }
        } else {
            System.out.println("Update has no message");
        }
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage1() {
        System.out.println("Scheduled task started at " + LocalDate.now() + " " + java.time.LocalTime.now());
        if(chatId != null) {
            sendMessage(chatId, MESSAGE_1);
        }
        else {
            System.out.println("Scheduled task ran, but no chatId set yet. Please write /start first!");
        }
    }


    public void sendMessage2() {
        System.out.println("Scheduled task started at " + LocalDate.now() + " " + java.time.LocalTime.now());
        if(chatId != null) {
            sendMessage(chatId, MESSAGE_2);
        }
        else {
            System.out.println("Scheduled task ran, but no chatId set yet. Please write /start first!");
        }
    }


    @Override
    public String getBotUsername() {
        System.out.println("Returning botUsername: " + botName);
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}

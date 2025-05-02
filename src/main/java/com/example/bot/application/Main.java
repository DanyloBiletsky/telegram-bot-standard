package com.example.bot.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class Main {

    private final String token;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public Main(@Value("${telegrambot.token}") String token) {
        this.token = token;
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public void handleUpdate(String updateJson) throws IOException, InterruptedException {
        Update update = mapper.readValue(updateJson, Update.class);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();
            String response;

            switch (text) {
                case "/start":
                    response = "Вітаю!";
                    break;
                case "/message1":
                    response = "один";
                    break;
                case "/message2":
                    response = "два";
                    break;
                default:
                    response = "Невідома команда";
            }

            sendMessage(chatId, response);
        }
    }

    private void sendMessage(String chatId, String text) throws IOException, InterruptedException {
        String url = "https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + chatId + "&text=" + text;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
package com.example.bot.application;

import com.amazonaws.services.lambda.runtime.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TelegramLambdaHandler implements RequestStreamHandler {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Main bot;

    static {
        // Ініціалізація Spring-контексту
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.example.bot");
        context.refresh();
        bot = context.getBean(Main.class);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try {
            // Читаємо вхідний потік як JSON
            String input = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            context.getLogger().log("Received input: " + input);

            // Обробляємо Telegram Update
            bot.handleUpdate(input);

            // Відповідь 200 OK
            String response = "{\"statusCode\": 200}";
            outputStream.write(response.getBytes());
        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            String errorResponse = "{\"statusCode\": 500, \"error\": \"" + e.getMessage() + "\"}";
            outputStream.write(errorResponse.getBytes());
        }
    }
}
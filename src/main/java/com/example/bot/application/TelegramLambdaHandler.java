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

public class TelegramLambdaHandler implements RequestStreamHandler{
    private static final ObjectMapper mapper = new ObjectMapper();
    private static ConfigurableApplicationContext context;
    private static Main bot;

    static {
        // Ініціалізація Spring-контексту з головним класом Spring Boot
        context = SpringApplication.run(TelegramApplication.class);
        bot = context.getBean(Main.class);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context lambdaContext) throws IOException {
        try {
            // Перевірка, чи це CloudWatch Event
            JsonNode input = mapper.readTree(inputStream);
            if (input.has("source") && input.get("source").asText().equals("aws.events")) {
                // Виклик розкладених методів

                bot.sendMessage1();
                bot.sendMessage2();
                String response = "{\"statusCode\": 200}";
                outputStream.write(response.getBytes());
                return;
            }

            // Обробка Telegram Update
            Update update = mapper.readValue(inputStream, Update.class);
            bot.onUpdateReceived(update);

            String response = "{\"statusCode\": 200}";
            outputStream.write(response.getBytes());
        } catch (Exception e) {
            lambdaContext.getLogger().log("Error processing request: " + e.getMessage());
            String errorResponse = "{\"statusCode\": 500, \"error\": \"" + e.getMessage() + "\"}";
            outputStream.write(errorResponse.getBytes());
        }
    }
}

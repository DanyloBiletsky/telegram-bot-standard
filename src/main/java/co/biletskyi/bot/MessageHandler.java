package co.biletskyi.bot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class MessageHandler implements RequestStreamHandler{
    //private final TelegramClient telegramClient;
    private final static Logger logger = (Logger) LogManager.getLogger(MessageHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TelegramBot bot = new TelegramBot(
            "@EchoDefaultBot",
            "YOUR_BOT_TOKEN",
            "/webhook"         // Шлях для Webhook
    );
    @Override
    public void handleRequest(InputStream inputStream,
                              OutputStream outputStream,
                              Context context) throws IOException {
        try {
            // Читаємо вхідний JSON
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            logger.info("Отримано запит: {}", json);

            // Парсимо Update
            Update update = objectMapper.readValue(json, Update.class);

            // Обробляємо оновлення
            var response = bot.onWebhookUpdateReceived(update);

            // Відправляємо відповідь (якщо є)
            if (response != null) {
                String responseJson = objectMapper.writeValueAsString(response);
                outputStream.write(responseJson.getBytes(StandardCharsets.UTF_8));
            } else {
                outputStream.write("{\"status\":\"ok\"}".getBytes(StandardCharsets.UTF_8));
            }

        } catch (Exception e) {
            logger.error("Помилка обробки запиту", e);
            outputStream.write("{\"error\":\"Internal Server Error\"}".getBytes(StandardCharsets.UTF_8));
            //throw new RuntimeException(e);
        }

//        String message = String.format("Hey, you wrote: %s", event.getBody().getMessage().getText());
//
//        try {
//            telegramClient.send(new Message(event.getBody().getMessage().getFrom().getId(), message));
//        } catch (MyTelegramException e) {
//            logger.error("Cannot send a message", e);
//        }
//
//        return Collections.singletonMap("statusCode", 200);
    }

//    private static String getEnvVariable(String key) {
//        String value = System.getenv(key);
//        if (value == null) {
//            String errorMessage = String.format("'%s' environment variable is not set", key);
//            logger.error(errorMessage);
//            throw new IllegalArgumentException(errorMessage);
//        }
//
//        return value;
//public MessageHandler() {
//    this.telegramClient = new TelegramClient(
//            HttpClient.newHttpClient(), getEnvVariable("botToken"), new ObjectMapper());
//}

//    }

}

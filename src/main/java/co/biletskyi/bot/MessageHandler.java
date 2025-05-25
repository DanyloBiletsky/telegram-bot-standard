package co.biletskyi.bot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MessageHandler implements RequestStreamHandler {
    private final static Logger logger = LogManager.getLogger(MessageHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            // here I'm trying to use also cyrillic alphabet
            .configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);
    private static final TelegramBot bot = new TelegramBot(
            // my data:
            "@...Bot",
            "token",
            "/...LambdaFunction"
    );
    @Override
    public void handleRequest(InputStream inputStream,
                              OutputStream outputStream,
                              Context context) throws IOException {
        try {
            // Читаємо вхідний JSON
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            logger.info("Отримано запит: {}", json);

            // Витягуємо body із запиту API Gateway
            Map<String, Object> request = objectMapper.readValue(json, Map.class);
            String body = (String) request.get("body");
            logger.info("Body: {}", body);

            // Десеріалізуємо body в Update
            Update update = objectMapper.readValue(body, Update.class);
            logger.info("Update: {}", update);

            // Обробляємо оновлення
            var response = bot.onWebhookUpdateReceived(update);

            // Відправляємо відповідь (якщо є)
            if (response != null) {
                String responseJson = objectMapper.writeValueAsString(response);
                logger.info("Відповідь: {}", responseJson);
                outputStream.write(responseJson.getBytes(StandardCharsets.UTF_8));
            } else {
                logger.info("Немає відповіді від бота");
                outputStream.write("{\"status\":\"ok\"}".getBytes(StandardCharsets.UTF_8));
            }

        } catch (Exception e) {
            logger.error("Помилка обробки запиту", e.getMessage(), e);
            outputStream.write("{\"error\":\"Internal Server Error\"}".getBytes(StandardCharsets.UTF_8));
        }

    }
}

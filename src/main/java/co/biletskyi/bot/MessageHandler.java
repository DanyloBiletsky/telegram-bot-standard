package co.biletskyi.bot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpClient;
import java.util.Collections;
import java.util.Map;

public class MessageHandler implements RequestStreamHandler{

    private final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private final TelegramClient telegramClient;

    public MessageHandler() {
        this.telegramClient = new TelegramClient(
                HttpClient.newHttpClient(), getEnvVariable("botToken"), new ObjectMapper());
    }

    @Override
    public Map<String, Integer> handleRequest(UpdateEvent event, Context context) {
        logger.info(event.toString());

        String message = String.format("Hey, you wrote: %s", event.getBody().getMessage().getText());

        try {
            telegramClient.send(new Message(event.getBody().getMessage().getFrom().getId(), message));
        } catch (MyTelegramException e) {
            logger.error("Cannot send a message", e);
        }

        return Collections.singletonMap("statusCode", 200);
    }

    private static String getEnvVariable(String key) {
        String value = System.getenv(key);
        if (value == null) {
            String errorMessage = String.format("'%s' environment variable is not set", key);
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        return value;
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

    }
}

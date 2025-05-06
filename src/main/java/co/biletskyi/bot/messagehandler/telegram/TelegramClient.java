package co.biletskyi.bot.messagehandler.telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TelegramClient {
    private final static String REQUEST_FORMAT = "https://api.telegram.org/bot%s/sendMessage";
    private final static int CLIENT_TIMEOUT_IN_MINUTES = 1;
    private final HttpClient httpClient;
    private final URI requestUri;
    private final ObjectMapper objectMapper;

    public TelegramClient(HttpClient httpClient,
                          String botToken,
                          ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        requestUri = URI.create(String.format(REQUEST_FORMAT, botToken));
        this.objectMapper = objectMapper;
    }

    public void send(Message message) throws MyTelegramException {
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(requestUri)
                    .timeout(Duration.ofMinutes(CLIENT_TIMEOUT_IN_MINUTES))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(message)))
                    .build();

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new MyTelegramException("Cannot send a message", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        if (response.statusCode() != 200) {
            throw new MyTelegramException(String.format("A message wasn't sent. Response body: %s", response.body()));
        }
    }
}

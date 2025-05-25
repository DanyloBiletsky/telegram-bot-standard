package co.biletskyi.bot;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class TelegramBot extends TelegramWebhookBot {

    private static final Logger logger = LogManager.getLogger(TelegramBot.class);

    private String botUsername;
    private String botToken;
    private String botPath;

    public TelegramBot(String botUsername,
                       String botToken,
                       String botPath) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.botPath = botPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String receivedText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            logger.info("Отримано повідомлення: {}", receivedText);

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            // Відповідаємо тим самим текстом та показуємо час на сервері:
            message.setText(receivedText + showServerTime());

            return message;
        }
        //if the message is not a text or does not exist:
        return null;
    }

    private String showServerTime() {
        ZoneId utcZone = ZoneId.of("UTC");
        LocalDateTime now = LocalDateTime.now(utcZone);
        return " (Час на сервері: " + now + ")";
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}

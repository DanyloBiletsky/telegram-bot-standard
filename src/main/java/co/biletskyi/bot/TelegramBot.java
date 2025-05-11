package co.biletskyi.bot;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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
            message.setText(receivedText); // Відповідаємо тим самим текстом

            return message;
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return null;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}

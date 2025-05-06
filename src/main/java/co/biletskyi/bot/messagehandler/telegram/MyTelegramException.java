package co.biletskyi.bot.messagehandler.telegram;

public class MyTelegramException extends Exception{
    private static final long serialVersionUID = -8705207557155600722L;

    public MyTelegramException(String message) {
        super(message);
    }

    public MyTelegramException(String message, Throwable cause) {
        super(message, cause);
    }
}

package co.biletskyi.bot.messagehandler.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {
    private String text;
    private From from;
}

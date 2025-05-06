package co.biletskyi.bot.messagehandler.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateEvent {
    private Body body;
}

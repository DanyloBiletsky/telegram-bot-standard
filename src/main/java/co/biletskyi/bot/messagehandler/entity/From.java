package co.biletskyi.bot.messagehandler.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class From {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
}

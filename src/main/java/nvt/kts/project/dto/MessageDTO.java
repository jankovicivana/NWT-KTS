package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {

    private String from;

    private String text;

    private String to;

    public MessageDTO(String from, String to, String text){
        this.from = from;
        this.text = text;
        this.to = to;
    }
}

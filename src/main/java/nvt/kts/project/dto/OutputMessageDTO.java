package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutputMessageDTO {

    private MessageClientDTO from;
    private String text;
    private MessageClientDTO to;

    public OutputMessageDTO(MessageClientDTO from, String text, MessageClientDTO to) {
        this.from = from;
        this.text = text;
        this.to = to;
    }
}

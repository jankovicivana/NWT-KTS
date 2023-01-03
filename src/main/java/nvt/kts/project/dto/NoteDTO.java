package nvt.kts.project.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDTO {
    private Long id;
    private String comment;
    private Long adminId;
    private Long clientId;
    private Long driverId;
}

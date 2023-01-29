package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;
import nvt.kts.project.model.Notification;

@Getter
@Setter
public class NotificationDTO {
    private Long driveId;
    private String message;
    private String reason;
    private String dateTime;


    public NotificationDTO(Notification n){
        this.driveId = n.getDrive().getId();
        this.message = n.getMessage();
        this.reason = n.getReason().name();
        this.dateTime = n.getDateTime().toString().split("T")[0]+' '+n.getDateTime().toString().split("T")[1];
    }
}

package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;
import nvt.kts.project.model.DriveStatus;
import nvt.kts.project.model.Notification;
import nvt.kts.project.model.NotificationReason;

@Getter
@Setter
public class NotificationDTO {
    private Long driveId;
    private String message;
    private String reason;
    private String dateTime;
    private String receiverEmail;
    private Boolean approvedPayment;
    private String driveStatus;


    public NotificationDTO(){}
    public NotificationDTO(Notification n){
        this.driveId = n.getDrive().getId();
        this.message = n.getMessage();
        this.reason = n.getReason().name();
        this.dateTime = n.getDateTime().toString().split("T")[0]+' '+n.getDateTime().toString().split("T")[1].substring(0,5);
        this.approvedPayment = false;
        this.driveStatus = n.getDrive().getStatus().toString();
    }

    public NotificationDTO(Notification n, DriveStatus status){
        this.driveId = n.getDrive().getId();
        this.message = n.getMessage();
        this.reason = n.getReason().name();
        this.dateTime = n.getDateTime().toString().split("T")[0]+' '+n.getDateTime().toString().split("T")[1].substring(0,5);
        this.approvedPayment = false;
        this.driveStatus = status.toString();
    }
}

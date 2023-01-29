package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleInfoDTO {
    private List<String> passengers;
    private String carType;
    private Boolean babiesAllowed;
    private Boolean petFriendly;
    private Double price;
    private Boolean splitFaire;
    private Boolean reservation;
}

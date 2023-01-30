package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleInfoDTO {
    private List<String> passengers;
    private String car;
    private Boolean babies;
    private Boolean pet;
    private Double price;
    private Boolean splitFaire;
    private Boolean reservation;
    private Double distance;
    private Double duration;
}

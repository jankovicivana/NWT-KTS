package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionDTO {
    private double lat;
    private double lon;
    private String address;
}

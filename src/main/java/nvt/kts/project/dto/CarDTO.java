package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarDTO {
    private boolean babiesAllowed;
    private boolean petFriendly;
    private String type;
    private Long driverId;
}
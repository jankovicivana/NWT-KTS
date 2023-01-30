package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDTO {
    private PositionDTO start;
    private PositionDTO end;
    private String type;
}

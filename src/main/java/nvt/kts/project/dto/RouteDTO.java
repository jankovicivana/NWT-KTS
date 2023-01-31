package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;
import nvt.kts.project.model.Route;

@Getter
@Setter
public class RouteDTO {
    private PositionDTO start;
    private PositionDTO end;
    private String type;

    public RouteDTO(Route r){
        this.end = new PositionDTO(r.getEndPosition());
        this.start = new PositionDTO(r.getStartPosition());
        this.type = r.getType();
    }
}

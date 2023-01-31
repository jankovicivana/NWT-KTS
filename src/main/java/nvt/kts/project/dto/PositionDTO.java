package nvt.kts.project.dto;

import javafx.geometry.Pos;
import lombok.Getter;
import lombok.Setter;
import nvt.kts.project.model.Position;

@Getter
@Setter
public class PositionDTO {
    private double lat;
    private double lon;
    private String address;

    public PositionDTO(){}
    public PositionDTO(Position p){
        this.address = p.getAddress();
        this.lat = p.getLat();
        this.lon = p.getLon();
    }
}

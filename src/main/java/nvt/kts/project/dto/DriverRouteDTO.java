package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;
import nvt.kts.project.model.Route;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DriverRouteDTO {

    private String username;

    private List<RouteDTO> routes;

    private String startTime;

    public DriverRouteDTO(String username, List<RouteDTO> routes, LocalDateTime startTime){
        this.username = username;
        this.routes = routes;
        this.startTime = startTime.toString();
    }
}

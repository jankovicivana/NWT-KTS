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

    private List<Route> routes;

    private LocalDateTime startTime;


    public DriverRouteDTO(String username, List<Route> routes, LocalDateTime startTime){
        this.username = username;
        this.routes = routes;
        this.startTime = startTime;
    }
}

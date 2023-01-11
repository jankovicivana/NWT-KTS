package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;
import nvt.kts.project.model.Route;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class DriverRouteDTO {

    private String username;

    private Set<Route> routes;

    private LocalDateTime startTime;


    public DriverRouteDTO(String username, Set<Route> routes, LocalDateTime startTime){
        this.username = username;
        this.routes = routes;
        this.startTime = startTime;
    }
}

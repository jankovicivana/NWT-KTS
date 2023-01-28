package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;
import nvt.kts.project.model.Route;
import org.springframework.security.web.server.authentication.logout.HeaderWriterServerLogoutHandler;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class DriveDTO {

    private Long id;

    private DriverDTO driver;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double price;

    private String status;

    private Set<Route> routes;

    private Set<ClientDriveDTO> passengers;

    private Double distance;

    public DriveDTO(Long id, DriverDTO driver, LocalDateTime startTime, LocalDateTime endTime, Double price, String status, Set<Route> routes,Double distance) {
        this.id = id;
        this.driver = driver;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.status = status;
        this.routes = routes;
        this.passengers = new HashSet<>();
        this.distance = distance;
    }
}

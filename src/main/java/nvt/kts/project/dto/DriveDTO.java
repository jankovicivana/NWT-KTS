package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;
import nvt.kts.project.model.Drive;
import nvt.kts.project.model.Route;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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

    private List<RouteDTO> routes;

    private Set<ClientDriveDTO> passengers;

    private Double distance;

    private String rejectionReason;

    public DriveDTO(Drive d, List<RouteDTO> routeList, DriverDTO driverDTO) {
        this.id = d.getId();
        this.driver = driverDTO;
        this.startTime = d.getStartTime();
        this.endTime = d.getEndTime();
        this.price = d.getPrice();
        this.status = d.getStatus().name();
        this.routes = routeList;
        this.passengers = new HashSet<>();
        this.distance = d.getDistance();
    }

}

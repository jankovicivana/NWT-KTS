package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.DriverRouteDTO;
import nvt.kts.project.model.Drive;
import nvt.kts.project.model.Driver;
import nvt.kts.project.model.Position;
import nvt.kts.project.model.Route;
import nvt.kts.project.service.DriveService;
import nvt.kts.project.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/drive")
public class DriveController {

    @Autowired
    private DriveService driveService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/current")
    public ResponseEntity<List<DriverRouteDTO>> getCurrentDrives() {
        List<Drive> drives = driveService.getCurrentDrives();
        List<DriverRouteDTO> routes = new ArrayList<>();
        for(Drive d: drives){
            Set<Route> routeList = d.getRoutes();
            for(Route r: routeList){
                r.setDrive(null);
            }
            routes.add(new DriverRouteDTO(d.getDriver().getUsername(), routeList, d.getStartTime()));
        }
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    @GetMapping("/positions")
    public ResponseEntity<Map<String, Position>> getDriversPositions() {
        Map<String, Position> driversMap = new HashMap<>();
        List<Driver> drivers = driverService.findAll();
        for(Driver d: drivers){
            if(d.isActive()) {
                driversMap.put(d.getUsername(), d.getPosition());
            }
        }
        return new ResponseEntity<>(driversMap, HttpStatus.OK);
    }

    @PostMapping("/start/{id}")
    public ResponseEntity<DriverRouteDTO> startDrive(@PathVariable("id") Long id){
        Drive drive = this.driveService.findById(id);
        DriverRouteDTO driverRouteDTO = null;
        if(drive != null) {
            Set<Route> routeList = drive.getRoutes();
            for (Route r : routeList) {
                r.setDrive(null);
            }
            driverRouteDTO = new DriverRouteDTO(drive.getDriver().getUsername(), routeList, LocalDateTime.now());
            this.simpMessagingTemplate.convertAndSend("/map-updates/new-drive", driverRouteDTO);
            return new ResponseEntity<>(driverRouteDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(driverRouteDTO, HttpStatus.BAD_REQUEST);
    }
}

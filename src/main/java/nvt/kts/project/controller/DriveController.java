package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.*;
import nvt.kts.project.model.*;
import nvt.kts.project.service.ClientService;
import nvt.kts.project.service.DriveService;
import nvt.kts.project.service.DriverService;
import nvt.kts.project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    private ClientService clientService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/current")
    public ResponseEntity<List<DriverRouteDTO>> getCurrentDrives() {
        List<Drive> drives = driveService.getCurrentDrives();
        List<DriverRouteDTO> routes = new ArrayList<>();
        for(Drive d: drives){
            List<Route> routeList = d.getRoutes();
            for(Route r: routeList){
                r.setDrive(null);
            }
            routes.add(new DriverRouteDTO(d.getDriver().getUsername(), routeList, d.getStartTime()));
        }
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    @GetMapping("/getAllClientDrives")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<List<DriveDTO>> getAllClientDrives(Principal principal) {
        List<Drive> drives = driveService.getClientDriveHistory(principal.getName());
        List<DriveDTO> drivesDTO = driveService.convertDriveToDTO(drives);
        return new ResponseEntity<>(drivesDTO, HttpStatus.OK);
    }

    @GetMapping("/getAllDriverDrives")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<List<DriveDTO>> getAllDriverDrives(Principal principal) {
        List<Drive> drives = driveService.getDriverDriveHistory(principal.getName());
        List<DriveDTO> drivesDTO = driveService.convertDriveToDTO(drives);
        return new ResponseEntity<>(drivesDTO, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<DriveDTO>> getAll(Principal principal) {
        List<Drive> drives = driveService.getAllDrives();
        List<DriveDTO> drivesDTO = driveService.convertDriveToDTO(drives);
        return new ResponseEntity<>(drivesDTO, HttpStatus.OK);
    }

    @PostMapping("/getAllByClientDate")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<List<DriveDTO>> getAllByClientDate(@RequestBody ReportDatesDTO reportDatesDTO,Principal principal) {
        LocalDateTime startDateTime = driveService.convertToLocalDateTime(reportDatesDTO.getStartDate());
        LocalDateTime endDateTime = driveService.convertToLocalDateTime(reportDatesDTO.getEndDate());

        List<Drive> drives = driveService.getAllClientDrivesByDate(startDateTime,endDateTime,principal.getName());
        List<DriveDTO> drivesDTO = driveService.convertDriveToDTO(drives);
        return new ResponseEntity<>(drivesDTO, HttpStatus.OK);
    }

    @PostMapping("/getAllByDriverDate")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<List<DriveDTO>> getAllByDriverDate(@RequestBody ReportDatesDTO reportDatesDTO,Principal principal) {
        LocalDateTime startDateTime = driveService.convertToLocalDateTime(reportDatesDTO.getStartDate());
        LocalDateTime endDateTime = driveService.convertToLocalDateTime(reportDatesDTO.getEndDate());

        List<Drive> drives = driveService.getAllDriverDrivesByDate(startDateTime,endDateTime,principal.getName());
        List<DriveDTO> drivesDTO = driveService.convertDriveToDTO(drives);
        return new ResponseEntity<>(drivesDTO, HttpStatus.OK);
    }

    @PostMapping("/getAllByDate")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<List<DriveDTO>> getAllByDate(@RequestBody ReportDatesDTO reportDatesDTO,Principal principal) {
        LocalDateTime startDateTime = driveService.convertToLocalDateTime(reportDatesDTO.getStartDate());
        LocalDateTime endDateTime = driveService.convertToLocalDateTime(reportDatesDTO.getEndDate());

        List<Drive> drives = driveService.getAllDrivesByDate(startDateTime,endDateTime,principal.getName());
        List<DriveDTO> drivesDTO = driveService.convertDriveToDTO(drives);
        return new ResponseEntity<>(drivesDTO, HttpStatus.OK);
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
        // promijeni u bazi pocetak voznje
        // mozda i stanje i poziciju vozaca da promijenimo
        DriverRouteDTO driverRouteDTO = null;
        if(drive != null) {
            List<Route> routeList = drive.getRoutes();
            for (Route r : routeList) {
                r.setDrive(null);
            }
            driverRouteDTO = new DriverRouteDTO(drive.getDriver().getUsername(), routeList, LocalDateTime.now());
            this.simpMessagingTemplate.convertAndSend("/map-updates/new-drive", driverRouteDTO);
            return new ResponseEntity<>(driverRouteDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(driverRouteDTO, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<Map<String, Position>> stopDrive(@PathVariable("id") Long id){
        Drive drive = this.driveService.findById(id);
        // mozda i stanje i poziciju vozaca da promijenimo
        // promijeni status voznje - cancelled

        if(drive != null) {
            Route route = drive.getRoutes().get(0);
            Position pos = route.getStartPosition();
            Map<String, Position> mapa =  new HashMap<>();
            mapa.put(drive.getDriver().getUsername(), pos);
            this.simpMessagingTemplate.convertAndSend("/map-updates/stop-drive", mapa);
            return new ResponseEntity<>(mapa, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/saveDrive")
    public ResponseEntity<Map<String, Position>> saveDrive(@RequestBody ScheduleInfoDTO info,Principal principal){
        //nekako treba sacuvati podatke
        Client client = clientService.getClientByEmail(principal.getName());
        Drive d = driveService.saveDrive(info,client);
        notificationService.sendNotificationsForApprovingPayment(d);
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/getDrivePriceByClient/{id}")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<ClientDriveDTO> getDrivePriceByClient(@PathVariable("id") Long driveId,Principal principal){
        Client client = clientService.getClientByEmail(principal.getName());
        Drive d = driveService.findById(driveId);  //rute uzmi
        ClientDrive cd = driveService.getClientDriveByInfo(client,driveId);
        ClientDriveDTO clientDriveDTO = new ClientDriveDTO();
        clientDriveDTO.setId(cd.getId());
        clientDriveDTO.setPrice(cd.getPrice());
        return new ResponseEntity<>(clientDriveDTO, HttpStatus.OK);
    }

    @GetMapping("/approvePayment/{id}")
    @PreAuthorize("hasAnyRole('client')")
    public ResponseEntity<Void> approvePayment(@PathVariable Long id,Principal principal) {
            Client c = clientService.getClientByEmail(principal.getName());
            if (driveService.approvePayment(id,c)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else
            {
                Drive drive = driveService.findDriveByClientDrive(id);
                driveService.rejectDrive(drive);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }


}

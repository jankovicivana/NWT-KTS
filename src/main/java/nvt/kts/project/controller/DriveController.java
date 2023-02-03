package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;

import nvt.kts.project.dto.*;
import nvt.kts.project.exception.DriverNotAvailableException;
import nvt.kts.project.model.*;
import nvt.kts.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private ReservationService reservationService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private RouteService routeService;

    @GetMapping("/current")
    public ResponseEntity<List<DriverRouteDTO>> getCurrentDrives() {
        List<Drive> drives = driveService.getCurrentDrives();
        List<DriverRouteDTO> routes = new ArrayList<>();
        for(Drive d: drives){
            List<RouteDTO> routeList = new ArrayList<>();
            for(Route r: d.getRoutes()){
                routeList.add(new RouteDTO(r));
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

    @GetMapping("/getFutureDriverDrives")
    @PreAuthorize("hasAnyRole('client','admin','driver')")
    public ResponseEntity<List<DriveDTO>> getFutureDriverDrives(Principal principal) {
        List<Drive> drives = driveService.getFutureDriverDrives(principal.getName());
        List<DriveDTO> drivesDTO = driveService.convertDriveToDTO(drives);
        return new ResponseEntity<>(drivesDTO, HttpStatus.OK);
    }

    @GetMapping("/getFavouriteDrives")
    @PreAuthorize("hasAnyRole('client')")
    public ResponseEntity<List<DriveDTO>> getFavouriteDrives(Principal principal) {
        List<Drive> drives = driveService.getFavouriteDrives(principal.getName());
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


    @GetMapping("/positionsActive")
    public ResponseEntity<Map<String, Position>> getActiveDriversPositions() {
        Map<String, Position> driversMap = new HashMap<>();
        List<Driver> drivers = driverService.findAll();
        for(Driver d: drivers){
            if(d.isActive()) {
                driversMap.put(d.getUsername(), d.getPosition());
            }
        }
        return new ResponseEntity<>(driversMap, HttpStatus.OK);
    }

    @GetMapping("/positionsInactive")
    public ResponseEntity<Map<String, Position>> getInactiveDriversPositions() {
        Map<String, Position> driversMap = new HashMap<>();
        List<Driver> drivers = driverService.findAll();
        for(Driver d: drivers){
            if(!d.isActive()) {
                driversMap.put(d.getUsername(), d.getPosition());
            }
        }
        return new ResponseEntity<>(driversMap, HttpStatus.OK);
    }

    @PostMapping("/start")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<String> startDrive(@RequestBody Long id){
        Drive drive = this.driveService.findById(id);
        if(drive == null){
            return new ResponseEntity<>("Drive id invalid.", HttpStatus.BAD_REQUEST);
        }
        if(drive.getStatus().equals(DriveStatus.IN_PROGRESS)){
            return new ResponseEntity<>("Drive already started.", HttpStatus.OK);
        }
        drive.setStatus(DriveStatus.IN_PROGRESS);
        drive.setStartTime(LocalDateTime.now());
        driveService.save(drive);
        clientService.setClientsDriving(drive.getPassengers(),true);
        notificationService.sendNotificationsForStartingDrive(drive);
        driverService.startDrive(drive);

        Map<String, Position> mapa = driveService.finishEmptyDrive(drive);
        if(!mapa.isEmpty()) {
            this.simpMessagingTemplate.convertAndSend("/map-updates/finish-drive", mapa);
        }
        DriverRouteDTO driverRouteDTO;
        List<RouteDTO> routeList = new ArrayList<>();
        for(Route r: drive.getRoutes()){
            routeList.add(new RouteDTO(r));
        }
        driverRouteDTO = new DriverRouteDTO(drive.getDriver().getUsername(), routeList, LocalDateTime.now());
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-drive", driverRouteDTO);
        return new ResponseEntity<>("Drive started.", HttpStatus.OK);
    }

    @PostMapping("/stop")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<String> stopDrive(@RequestBody Long id){
        Drive drive = this.driveService.findById(id);
        if(drive == null){
            return new ResponseEntity<>("Drive id invalid.", HttpStatus.BAD_REQUEST);
        }
        if(drive.getStatus().equals(DriveStatus.STOPPED)){
            return new ResponseEntity<>("Drive already stopped.", HttpStatus.OK);
        }
        drive.setStatus(DriveStatus.STOPPED);
        driveService.save(drive);
        clientService.setClientsDriving(drive.getPassengers(),false);
        notificationService.sendNotificationsForStoppingDrive(drive);
        Driver driver = drive.getDriver();
        driver.setAvailable(true);
        driverService.save(driver);

        Map<String, Position> mapa =  new HashMap<>();
        mapa.put(driver.getUsername(), driver.getPosition());
        this.simpMessagingTemplate.convertAndSend("/map-updates/stop-drive", mapa);
        return new ResponseEntity<>("Drive stopped.", HttpStatus.OK);
    }

    @PostMapping("/finish")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<String> finishDrive(@RequestBody Long id){
        Drive drive = this.driveService.findById(id);
        if(drive == null){
            return new ResponseEntity<>("Drive id invalid.", HttpStatus.BAD_REQUEST);
        }
        if(drive.getStatus().equals(DriveStatus.FINISHED)){
            return new ResponseEntity<>("Drive already finished", HttpStatus.OK);
        }
        drive.setStatus(DriveStatus.FINISHED);
        driveService.save(drive);
        clientService.setClientsDriving(drive.getPassengers(),false);
        notificationService.sendNotificationsForFinishedDrive(drive);
        Driver driver = driverService.finishDrive(drive);
        Map<String, Position> positionMap =  new HashMap<>();
        positionMap.put(driver.getUsername(), driver.getPosition());
        this.simpMessagingTemplate.convertAndSend("/map-updates/finish-drive", positionMap);
        return new ResponseEntity<>("Drive finished.", HttpStatus.OK);
    }

    @PostMapping("/saveDrive")
    public ResponseEntity<String> saveDrive(@RequestBody ScheduleInfoDTO info,Principal principal){
        Client client = clientService.getClientByEmail(principal.getName());
        Drive d = driveService.saveDrive(info, client);
        if (Boolean.FALSE.equals(info.getReservation())){
            notificationService.sendNotificationsForApprovingPayment(d);
        }
        return new ResponseEntity<>("Successfully saved drive.", HttpStatus.OK);
    }

    @PostMapping("/goToClient")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<String> goToClient(@RequestBody EmptyDriveDTO dto, Principal principal){
        Drive d = driveService.findById(dto.getDrive().getId());
        d.setStatus(DriveStatus.GOING_TO_CLIENT);
        driveService.save(d);
        DriverRouteDTO driverRouteDTO = driveService.saveEmptyDrive(d, dto.getDuration());
        this.notificationService.sendNotificationGoingToClient(d);
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-drive", driverRouteDTO);
        return new ResponseEntity<>("Driver going to client.", HttpStatus.OK);
    }


    @PostMapping("/saveRejectionDriveReason")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<String> saveRejectionDriveReason(@RequestBody DriveDTO drive, Principal principal){
        Drive d = driveService.rejectDrive(drive);
        driveService.save(d);

        Map<String, Position> mapa = driveService.stopEmptyDrive(d);
        if(!mapa.isEmpty()) {
            this.simpMessagingTemplate.convertAndSend("/map-updates/stop-drive", mapa);
        }
        notificationService.sendNotificationForDriverRejectingDrive(d);
        return new ResponseEntity<>("Super", HttpStatus.OK);
    }

    @PostMapping("/saveReport")
    public ResponseEntity<String> saveReport(@RequestBody ReportDTO dto,Principal principal){
        Drive d = driveService.findById(dto.getDriveId());
        Client c = clientService.getClientByEmail(principal.getName());
        Report r = new Report();
        r.setDrive(d);
        r.setClient(c);
        r.setComment(dto.getComment());
        reportService.save(r);
        return new ResponseEntity<>("Super", HttpStatus.OK);
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

    @GetMapping("/checkIfAllApproved/{id}")
    public ResponseEntity<Void> checkIfAllApproved(@PathVariable("id") Long clientDriveId){
        Drive drive = driveService.findDriveByClientDrive(clientDriveId);
        if (driveService.checkIfAllPassengersApprovedPayment(drive)){
            Driver driver;
            try {
                 driver = driverService.findAvailableDriver(drive);
            }catch (DriverNotAvailableException e){
                driveService.rejectDriveNoDriver(drive);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (driveService.checkIfAllCanPay(drive)){
                driveService.payDrive(drive);
                drive.setDriver(driver);
                driveService.saveScheduledDrive(drive);
                driver.setAvailable(false);
                driverService.save(driver);
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }else {
                driveService.rejectDriveNoEnoughTokens(drive);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
                driveService.rejectDriveNoEnoughTokens(drive);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }

    @GetMapping("/getCurrentDrive")
    @PreAuthorize("hasAnyRole('client')")
    public ResponseEntity<DriveDTO> getCurrentDrive(Principal principal) {
            Client c = clientService.getClientByEmail(principal.getName());
            Drive currentDrive = driveService.getCurrentClientDrive(c.getEmail());
            List<Drive> drive = new ArrayList<>();
            drive.add(currentDrive);
            DriveDTO dto = driveService.convertDriveToDTO(drive).get(0);
            return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @Scheduled(cron = "${reminder.cron}")
    public void cronJob(){

        this.sendDriveUpdates();

        List<Reservation> reservationsIn15 = this.reservationService.getReservationsIn(15L);
        List<Reservation> reservationsIn10 = this.reservationService.getReservationsIn(10L);
        List<Reservation> reservationsIn5 = this.reservationService.getReservationsIn(5L);

        for(Reservation r: reservationsIn15){
            this.notificationService.sendReservationReminder(r, 15);
            if(r.getDrive().getStatus().equals(DriveStatus.RESERVED)){
                notificationService.sendNotificationsForApprovingPayment(r.getDrive());
            }
        }

        for(Reservation r: reservationsIn10){
            if(r.getDrive().getStatus().equals(DriveStatus.RESERVED) || r.getDrive().getStatus().equals(DriveStatus.SCHEDULED)){
                this.notificationService.sendReservationReminder(r, 10);
            }
        }

        for(Reservation r: reservationsIn5){
            if(r.getDrive().getStatus().equals(DriveStatus.RESERVED) || r.getDrive().getStatus().equals(DriveStatus.SCHEDULED)){
                this.notificationService.sendReservationReminder(r, 5);
            }
        }
    }

    private void sendDriveUpdates() {
        List<Drive> emptyDrives = this.driveService.getEmptyDrives();
        for(Drive d: emptyDrives){
            List<Route> routes = this.routeService.getRoutes(d.getId());
            Drive scheduled = this.driveService.getScheduledDrive(d.getDriver().getEmail(), routes.get(0).getEndPosition());
            int duration = (int) d.getDuration();
            LocalDateTime start = d.getStartTime();
            LocalDateTime end = start.plusMinutes(duration);
            LocalDateTime now = LocalDateTime.now();

            if(end.isAfter(now)){
                long timeLeft = now.until(end, ChronoUnit.MINUTES);
                this.notificationService.sendNotificationTimeLeft(scheduled, timeLeft);
            }
        }
    }


}

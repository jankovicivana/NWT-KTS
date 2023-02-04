package nvt.kts.project.service;

import nvt.kts.project.dto.*;
import nvt.kts.project.model.*;
import nvt.kts.project.repository.ClientDriveRepository;
import nvt.kts.project.repository.DriveRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DriveService {

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private SystemInfoService systemInfoService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CarService carService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ClientDriveRepository clientDriveRepository;

    @Autowired
    private ReservationService reservationService;

    public List<Drive> getCurrentDrives() {
        return driveRepository.getCurrentDrives(LocalDateTime.now());
    }

    public List<Drive> getClientDriveHistory(String email) {
        return driveRepository.getClientDriveHistory(LocalDateTime.now(),email);
    }

    public List<Drive> getDriverDriveHistory(String email) {
        return driveRepository.getDriverDriveHistory(LocalDateTime.now(),email);
    }

    public List<Drive> getAllDrives(){
        return driveRepository.getAllDrives(LocalDateTime.now());
    }

    public List<Drive> getAllClientDrivesByDate(LocalDateTime start, LocalDateTime end, String email){
        return driveRepository.getDrivesByClientDate(start,end,email);
    }
    public List<Drive> getAllDriverDrivesByDate(LocalDateTime start, LocalDateTime end, String email){
        return driveRepository.getDrivesByDriverDate(start,end,email);
    }
    public List<Drive> getAllDrivesByDate(LocalDateTime start, LocalDateTime end, String email){
        return driveRepository.getDrivesByDate(start,end);
    }

    public LocalDateTime convertToLocalDateTime(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date+" 00:00", formatter);
    }

    public Drive findById(Long id) {
        return driveRepository.findById(id).orElse(null);
    }

    public List<DriveDTO> convertDriveToDTO(List<Drive> drives){
        List<DriveDTO> drivesDTO = new ArrayList<>();
        for (Drive d:drives){
            List<RouteDTO> routeList = new ArrayList<>();
            for(Route r: d.getRoutes()){
                routeList.add(new RouteDTO(r));
            }


            DriverDTO driverDTO = null;
            if(d.getDriver() != null)
                 driverDTO = mapper.map(d.getDriver(),DriverDTO.class);

            DriveDTO driveDTO = new DriveDTO(d, routeList, driverDTO, d.getCreatedTime());
            Set<ClientDriveDTO> passengers = new HashSet<>();

            for(ClientDrive cd:d.getPassengers()){
                passengers.add(new ClientDriveDTO(cd.getId(),cd.getClient().getName(),cd.getClient().getSurname(),cd.getClient().getEmail(),cd.getPrice()));
            }
            driveDTO.setPassengers(passengers);
            drivesDTO.add(driveDTO);
        }
        return drivesDTO;
    }

    public Drive saveDrive(ScheduleInfoDTO info,Client loggedUser) {

        Drive d = new Drive();
        d.setPrice(info.getPrice());

        d.setStatus(DriveStatus.SCHEDULING_IN_PROGRESS);
        d.setDuration(info.getDuration());
        d.setCarType(carService.findCarTypeByName(info.getCar()));
        d.setBabiesAllowed(info.getBabies());
        d.setPetFriendly(info.getPet());
        d.setCreatedTime(LocalDateTime.now());
        driveRepository.save(d);
        if(Boolean.TRUE.equals(info.getReservation())){
            Reservation r = new Reservation();
            r.setDrive(d);
            r.setStart(LocalDateTime.parse(info.getReservationTime().replace("Z", "")).plusHours(1));
            r.setExpectedDuration(info.getDuration());
            Reservation saved = this.reservationService.save(r);
            d.setReservation(saved);
            d.setStatus(DriveStatus.RESERVED);
            driveRepository.save(d);
        }

        saveRoutes(info.getRoutes(), d);
        clientDriveRepository.save(createClientDrive(d,loggedUser,info,true, info.isFavourite()));
        for (String email: info.getPassengers()){
            Client c = clientService.getClientByEmail(email);
            clientDriveRepository.save(createClientDrive(d,c,info,false, false));
        }
        return d;
    }

    public void saveRoutes(List<RouteDTO> routes, Drive d) {
        if (d.getRoutes() == null){
            d.setRoutes(new ArrayList<>());
        }
        for(RouteDTO dto: routes){
            Route r = new Route();
            r.setDrive(d);
            Position start = new Position();
            start.setLat(dto.getStart().getLat());
            start.setLon(dto.getStart().getLon());
            start.setAddress(dto.getStart().getAddress());

            Position end = new Position();
            end.setLat(dto.getEnd().getLat());
            end.setLon(dto.getEnd().getLon());
            end.setAddress(dto.getEnd().getAddress());

            r.setStartPosition(start);
            r.setEndPosition(end);
            r.setType(dto.getType());
            d.getRoutes().add(r);
            this.routeService.save(r);
        }
    }

    public ClientDrive createClientDrive(Drive d,Client client,ScheduleInfoDTO info,Boolean logged, Boolean favourite){
        ClientDrive cd = new ClientDrive();
        cd.setDrive(d);
        cd.setClient(client);
        cd.setFavourite(favourite);
        if (!info.getSplitFaire()){
            double splitFairePrice = info.getPrice()/(info.getPassengers().size()+1);
            cd.setPrice(splitFairePrice);
        }else {
            if (logged){
                cd.setPrice(info.getPrice());
            }
            else{
                cd.setPrice(0);
                cd.setApproved(true);
            }
        }
        return cd;
    }

    public ClientDrive getClientDriveByInfo(Client client, Long driveId) {
        return clientDriveRepository.getClientDriveByDriveAndClient(client.getId(),driveId);
    }

    public List<Drive> getDriverCurrentDrive(String mail) {
        return this.driveRepository.getCurrentDriverDrive(mail);
    }

    public Drive getCurrentClientDrive(String mail) {
        List<Drive> drives =  this.driveRepository.getCurrentClientDrive(mail);
        if(drives.size() == 0){
            return null;
        }
        return drives.get(0);
    }

    public boolean approvePayment(Long id, Client c) {
        ClientDrive cd  = clientDriveRepository.findById(id).orElse(null);
        if (cd != null){
            if (c.getTokens()*systemInfoService.getTokenPrice() > cd.getPrice()){
                cd.setApproved(true);
                clientDriveRepository.save(cd);
                return true;
            }
            else
            {return false;}
        }
        return false;
    }

    public Drive findDriveByClientDrive(Long id) {
        ClientDrive cd  = clientDriveRepository.findById(id).orElse(null);
        if (cd != null){
            return cd.getDrive();
        }
        return null;
    }

    public void rejectDriveNoEnoughTokens(Drive drive) {
        drive.setStatus(DriveStatus.REJECTED);
        driveRepository.save(drive);
        notificationService.sendNotificationForRejectingDriveNotEnoughTokens(drive);
    }

    public boolean checkIfAllPassengersApprovedPayment(Drive drive) {
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(drive.getId());
        for(ClientDrive cd:clientDrives){
            if(!cd.isApproved()) {return false;}
        }
        return true;
    }

    public boolean checkIfAllCanPay(Drive drive) {
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(drive.getId());
        Double tokenPrice = systemInfoService.getTokenPrice();
        for (ClientDrive cd: clientDrives){
            if(cd.getClient().getTokens()*tokenPrice<cd.getPrice()){return false;}
        }
        return true;
    }

    public void payDrive(Drive drive) {
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(drive.getId());
        Double tokenPrice = systemInfoService.getTokenPrice();
        for (ClientDrive cd: clientDrives){
            cd.getClient().setTokens(cd.getClient().getTokens() - cd.getPrice()/tokenPrice);
            clientService.save(cd.getClient());
        }
    }

    public void saveScheduledDrive(Drive drive) {
        drive.setStatus(DriveStatus.SCHEDULED);
        driveRepository.save(drive);
        notificationService.sendNotificationForAcceptingDrive(drive);
    }

    public void rejectDriveNoDriver(Drive drive) {
        drive.setStatus(DriveStatus.REJECTED);
        driveRepository.save(drive);
        notificationService.sendNotificationForRejectingDriveNoAvailableDriver(drive);
    }

    public boolean hasFutureReservations(Driver d, Drive drive) {
        double duration = drive.getDuration()+5;
        LocalDateTime bound;
        if (drive.getReservation() == null){
            bound = LocalDateTime.now().plus((long) duration, ChronoUnit.MINUTES);}
        else {
            bound = drive.getReservation().getStart().plus((long) duration, ChronoUnit.MINUTES);
        }
        List<Drive> reservations = driveRepository.getReservations(d.getId(),bound,LocalDateTime.now());
        return reservations.size() != 0;
    }

    public Drive save(Drive d) {
        return driveRepository.save(d);
    }

    public List<Drive> getFutureDriverDrives(String name) {
        return driveRepository.getFutureDriverDrives(name);
    }

    public List<Drive> getFavouriteDrives(String name) {
        List<Drive> favourites = new ArrayList<>();
        for(ClientDrive cd: clientDriveRepository.getFavouriteDrives(name)){
            Drive d = cd.getDrive();
            favourites.add(d);
        }
        return favourites;
    }

    public Drive getDriverEmptyDrive(String username, String address) {
        List<Drive> drives = this.driveRepository.getDriverEmptyDrives(username);
        for(Drive d: drives){
            if(d.getRoutes().get(0).getEndPosition().getAddress().equals(address)){
                return d;
            }
        }
        return null;
    }

    public List<Drive> getEmptyDrives() {
        return this.driveRepository.getEmptyDrives();
    }

    public Drive getScheduledDrive(String email, Position endPosition) {
        List<Drive> drives = this.driveRepository.getGoingToClientDrives(email);
        for(Drive d: drives){
            List<Route> routes = this.routeService.getRoutes(d.getId());
            if(routes.get(0).getStartPosition().getAddress().equals(endPosition.getAddress())){
                return d;
            }
        }
        return null;
    }

    public DriverRouteDTO saveEmptyDrive(Drive d, double duration) {
        Drive newDrive = new Drive();
        newDrive.setDriver(d.getDriver());
        newDrive.setDuration(duration);
        newDrive.setPrice(0);
        newDrive.setPassengers(new ArrayList<>());
        newDrive.setStatus(DriveStatus.EMPTY);
        newDrive.setCreatedTime(LocalDateTime.now());
        newDrive.setStartTime(LocalDateTime.now());
        Drive updated = save(newDrive);
        Route r = new Route();
        r.setStartPosition(d.getDriver().getPosition());
        r.setEndPosition(d.getRoutes().get(0).getStartPosition());
        r.setType("recommended");
        r.setDrive(updated);
        this.routeService.save(r);
        List<Route> routes = new ArrayList<>();
        routes.add(r);
        updated.setRoutes(routes);
        save(updated);

        List<RouteDTO> routeList = new ArrayList<>();
        for(Route route: updated.getRoutes()){
            routeList.add(new RouteDTO(route));
        }
        return new DriverRouteDTO(updated.getDriver().getUsername(), routeList, LocalDateTime.now());
    }

    public Map<String, Position> finishEmptyDrive(Drive drive) {
        Drive empty = getDriverEmptyDrive(drive.getDriver().getUsername(), drive.getRoutes().get(0).getStartPosition().getAddress());
        if(empty != null) {
            empty.setStatus(DriveStatus.FINISHED);
            save(empty);
            Route route = empty.getRoutes().get(0);
            Position pos = route.getEndPosition();
            Map<String, Position> mapa = new HashMap<>();
            mapa.put(drive.getDriver().getUsername(), pos);
            return  mapa;
        }
        return Collections.emptyMap();
    }

    public Map<String, Position> stopEmptyDrive(Drive drive) {
        Drive empty = getDriverEmptyDrive(drive.getDriver().getUsername(), drive.getRoutes().get(0).getStartPosition().getAddress());
        if (empty != null) {
            empty.setStatus(DriveStatus.FINISHED);
            save(empty);
            Route route = empty.getRoutes().get(0);
            Position pos = route.getStartPosition();
            Map<String, Position> mapa = new HashMap<>();
            mapa.put(drive.getDriver().getUsername(), pos);
            return mapa;
        }
        return Collections.emptyMap();
    }

    public Drive rejectDrive(DriveDTO drive) {
        Drive d = findById(drive.getId());
        d.setRejectionReason(drive.getRejectionReason());
        d.setStatus(DriveStatus.REJECTED);
        save(d);
        return d;
    }
}

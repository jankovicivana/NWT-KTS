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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            List<Route> routeList = d.getRoutes();

            for(Route r: routeList){
                r.setDrive(null);
            }
            DriverDTO driverDTO = mapper.map(d.getDriver(),DriverDTO.class);
            DriveDTO driveDTO = new DriveDTO(d, routeList, driverDTO);
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
        driveRepository.save(d);
        if(Boolean.TRUE.equals(info.getReservation())){
            Reservation r = new Reservation();
            r.setDrive(d);
            r.setStart(LocalDateTime.parse(info.getReservationTime().replace("Z", "")).plusHours(1));
            r.setExpectedDuration(info.getDuration());
            Reservation saved = this.reservationService.save(r);
            d.setReservation(saved);
            driveRepository.save(d);
        }

        saveRoutes(info.getRoutes(), d);
        clientDriveRepository.save(createClientDrive(d,loggedUser,info,true));
        for (String email: info.getPassengers()){
            Client c = clientService.getClientByEmail(email);
            clientDriveRepository.save(createClientDrive(d,c,info,false));
        }
        return d;
    }

    private void saveRoutes(List<RouteDTO> routes, Drive d) {
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
            this.routeService.save(r);
        }
    }

    public ClientDrive createClientDrive(Drive d,Client client,ScheduleInfoDTO info,Boolean logged){
        ClientDrive cd = new ClientDrive();
        cd.setDrive(d);
        cd.setClient(client);
        if (info.getSplitFaire()){
            double splitFairePrice = info.getPrice()/(info.getPassengers().size()+1);
            cd.setPrice(splitFairePrice);
        }else {
            if (logged != null){
                cd.setPrice(info.getPrice());
            }
            else{
                cd.setPrice(0);
            }
        }
        return cd;
    }

    public ClientDrive getClientDriveByInfo(Client client, Long driveId) {
        return clientDriveRepository.getClientDriveByDriveAndClient(client.getId(),driveId);
    }

    public Drive getDriverCurrentDrive(String mail) {
        return this.driveRepository.getCurrentDriverDrive(mail);
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

    public void rejectDrive(Drive drive) {
        drive.setStatus(DriveStatus.REJECTED);
        driveRepository.save(drive);
        notificationService.sendNotificationForRejectingDriveNotEnoghTokens(drive);
    }

    public boolean checkIfAllPassengersApprovedPayment(Drive drive) {
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(drive.getId());
        for(ClientDrive cd:clientDrives){
            if(!cd.isApproved()) {return false;}
        }
        return true;
    }
}

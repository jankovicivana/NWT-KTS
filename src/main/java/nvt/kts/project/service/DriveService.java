package nvt.kts.project.service;

import nvt.kts.project.dto.ClientDriveDTO;
import nvt.kts.project.dto.DriveDTO;
import nvt.kts.project.dto.DriverDTO;
import nvt.kts.project.dto.ScheduleInfoDTO;
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
    private ClientDriveRepository clientDriveRepository;

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
        List<ClientDrive> set = new ArrayList<>();
        Drive d = new Drive();
        d.setPrice(info.getPrice());
        d.setStatus(DriveStatus.SCHEDULING_IN_PROGRESS);
        set.add(createClientDrive(d,loggedUser,info,true));
        for (String email: info.getPassengers()){
            Client c = clientService.getClientByEmail(email);
            set.add(createClientDrive(d,c,info,false));
        }
        d.setPassengers(set);
        driveRepository.save(d);
        return d;
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
}

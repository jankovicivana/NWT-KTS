package nvt.kts.project.service;

import nvt.kts.project.dto.ClientDriveDTO;
import nvt.kts.project.dto.DriveDTO;
import nvt.kts.project.dto.DriverDTO;
import nvt.kts.project.model.ClientDrive;
import nvt.kts.project.model.Drive;
import nvt.kts.project.model.Route;
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

}

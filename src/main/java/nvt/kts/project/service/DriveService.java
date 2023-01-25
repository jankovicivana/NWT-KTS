package nvt.kts.project.service;

import nvt.kts.project.dto.ClientDTO;
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

    public Drive findById(Long id) {
        return driveRepository.findById(id).orElse(null);
    }

    public List<DriveDTO> convertDriveToDTO(List<Drive> drives){
        List<DriveDTO> drivesDTO = new ArrayList<>();
        for (Drive d:drives){
            Set<Route> routeList = d.getRoutes();

            for(Route r: routeList){
                r.setDrive(null);
            }
            DriverDTO driverDTO = mapper.map(d.getDriver(),DriverDTO.class);
            DriveDTO driveDTO = new DriveDTO(d.getId(), driverDTO,d.getStartTime(),d.getEndTime(),d.getPrice(),d.getStatus().name(),routeList);
            Set<ClientDTO> passengers = new HashSet<>();
            for(ClientDrive cd:d.getPassengers()){
                passengers.add(mapper.map(cd.getClient(),ClientDTO.class));
            }
            driveDTO.setPassengers(passengers);
            drivesDTO.add(driveDTO);
        }
        return drivesDTO;
    }

}

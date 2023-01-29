package nvt.kts.project.service;


import nvt.kts.project.dto.*;
import nvt.kts.project.model.Drive;
import nvt.kts.project.model.Driver;
import nvt.kts.project.model.DriverActivity;
import nvt.kts.project.model.Role;
import nvt.kts.project.repository.DriverActivityRepository;
import nvt.kts.project.repository.DriverRepository;
import nvt.kts.project.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverActivityRepository driverActivityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CarService carService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Driver save(Driver driver){ return driverRepository.save(driver);}

    public Driver save(UserRequest userRequest) {
        Driver driver = new Driver();
        driver.setEmail(userRequest.getEmail());
        driver.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        driver.setName(userRequest.getName());
        driver.setSurname(userRequest.getSurname());
        driver.setCity(userRequest.getCity());
        driver.setEnabled(false);
        driver.setPhoneNumber(userRequest.getPhoneNumber());
        List<Role> roles = roleRepository.findByName("ROLE_" + userRequest.getRole().toLowerCase(Locale.ROOT));
        driver.setRoles(roles);
        driver.setPhoto("unknown.jpg");

        return this.driverRepository.save(driver);
    }

    public List<Driver> getDrivers(Pageable pageable, HttpHeaders hh) {
        Page<Driver> drivers =driverRepository.findAllByDeletedIsFalse(pageable);
        hh.add("Total-items", Long.toString(drivers.getTotalElements()));
        return drivers.getContent();
    }

    public Driver mapDriverInfo(DriverCarDTO dto, Driver driver) {
        DriverDTO updatedDriverDTO = new DriverDTO(dto, driver);
        Driver updatedDriver = mapper.map(updatedDriverDTO,Driver.class);
        updatedDriver.setCar(driver.getCar());
        updatedDriver.getCar().setBabiesAllowed(dto.getBabiesAllowed());
        updatedDriver.getCar().setPetFriendly(dto.getPetFriendly());
        updatedDriver.getCar().setType(carService.findCarTypeById(dto.getType()));
        updatedDriver.setRoles(driver.getRoles());
        return updatedDriver;
    }

    public Driver findDriverById(Long id) {
        return driverRepository.findDriverById(id);
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public Driver getDriverByEmail(String email) {
        return driverRepository.findByEmail(email);
    }

    public Driver getDriverById(Long id) {
        return driverRepository.findDriverById(id);
    }

    public List<Driver> getActiveDriversByCarCriteria(CarDTO dto) {
        List<Driver> activeDrivers = driverRepository.findActiveDriversByCarCriteria(dto.getType(),dto.isBabiesAllowed(),dto.isPetFriendly());
        System.out.print(activeDrivers);
        return activeDrivers;
    }

    public void createActivityLog(Driver d) {
        DriverActivity activity = new DriverActivity();
        activity.setDriver(d);
        activity.setStartTime(LocalDateTime.now());
        driverActivityRepository.save(activity);
    }

    public void finishActivityLog(Driver d) {
        DriverActivity activity = driverActivityRepository.findUnfinishedLog(d.getId());
        activity.setEndTime(LocalDateTime.now());
        driverActivityRepository.save(activity);
    }

    public boolean isActive(Driver d) {
        DriverActivity activity = driverActivityRepository.findUnfinishedLog(d.getId());
        return activity != null;
    }

    public Boolean hasWorkingHours(Driver d) {
        long workingMinutes = 0;
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        List<DriverActivity> activities = driverActivityRepository.getDriverActivites(d.getId());
        for (DriverActivity activity: activities){
            if(activity.getStartTime().isAfter(from) && (activity.getEndTime() == null || activity.getEndTime().isAfter(to))){
                workingMinutes += activity.getStartTime().until(to, ChronoUnit.MINUTES);
            }
            else if(activity.getStartTime().isBefore(from) && (activity.getEndTime() == null || activity.getEndTime().isAfter(to))){
                workingMinutes += from.until(to, ChronoUnit.MINUTES);
            }
            else if(activity.getStartTime().isAfter(from) && activity.getEndTime().isBefore(to)){
                workingMinutes += activity.getStartTime().until(activity.getEndTime(), ChronoUnit.MINUTES);
            }
        }
        return workingMinutes < 8*60;
    }


}

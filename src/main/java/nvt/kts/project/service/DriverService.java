package nvt.kts.project.service;
import nvt.kts.project.dto.*;
import nvt.kts.project.exception.DriverNotAvailableException;
import nvt.kts.project.model.*;
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

    @Autowired
    private DriveService driveService;

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
        CarType type = carService.findCarTypeByName(dto.getType());
        if(type == null){
            type = carService.findCarTypeById(dto.getType());
        }
        updatedDriver.getCar().setType(type);
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

    public List<Driver> getActiveAndAvailableDriversByCarCriteria(CarDTO dto) {
        return driverRepository.findActiveAndAvailableDriversByCarCriteria(dto.getType(),dto.isBabiesAllowed(),dto.isPetFriendly());
    }

    public List<Driver> getActiveDriversByCarCriteria(CarDTO dto) {
        return driverRepository.findActiveDriversByCarCriteria(dto.getType(),dto.isBabiesAllowed(),dto.isPetFriendly());
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

    public Driver getNearestDriver(List<Driver> drivers, Position position){
        double minDistance = Double.POSITIVE_INFINITY;
        Driver nearest = null;
        for(Driver d: drivers){
            Position p = d.getPosition();
            double dis = distance(p.getLat(), p.getLon(), position.getLat(), position.getLon());
            if(dis < minDistance){
                minDistance = dis;
                nearest =d;
            }
        }
        return nearest;
    }

    public Driver getDriverNearestToEnd(List<Driver> drivers){
        long minTime = Long.MAX_VALUE;
        Driver nearest = null;
        for(Driver d: drivers){
            Drive current = driveService.getDriverCurrentDrive(d.getEmail()).get(0);
            LocalDateTime start = current.getStartTime();
            double duration = current.getDuration();
            LocalDateTime expectedEnd;
            if (start == null){
                expectedEnd = LocalDateTime.now().plusMinutes((long) duration);
            }else{
                expectedEnd = start.plusMinutes((long) duration);  //start moze biti null}
                 }
            long diff = LocalDateTime.now().until(expectedEnd, ChronoUnit.MINUTES);
            if (diff < minTime){
                minTime = diff;
                nearest = d;
            }
        }
        return nearest;
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public Driver findAvailableDriver(Drive drive) {
        List<Driver> drivers = getActiveAndAvailableDriversByCarCriteria(new CarDTO(drive.isPetFriendly(),drive.isBabiesAllowed(),drive.getCarType().getType()));
        if (drivers.size() != 0){
            Driver d = findNearestDriverWithWorkingHours(drivers, drive);
            if ( d!= null ){
                return d;
            }
        }
        List<Driver> activeDrivers = getActiveDriversByCarCriteria(new CarDTO(drive.isPetFriendly(),drive.isBabiesAllowed(),drive.getCarType().getType()));
        if (activeDrivers.size() == 0){
            throw new DriverNotAvailableException();
        }
        else{
            return findDriverNearestToEndWithAvailableWorkingHours(activeDrivers,drive);
        }

    }

    public Driver findNearestDriverWithWorkingHours(List<Driver> drivers,Drive drive){
        while (true){
            Driver d = getNearestDriver(drivers, drive.getRoutes().get(0).getStartPosition());
            if (hasWorkingHours(d) && !driveService.hasFutureReservations(d, drive)){
                return d;
            }
            drivers.remove(d);
            if (drivers.size() == 0){return null;}
        }


    }

    public Driver findDriverNearestToEndWithAvailableWorkingHours(List<Driver> drivers,Drive drive){
        while (true){
            Driver d = getDriverNearestToEnd(drivers);
            if (hasWorkingHours(d) && !driveService.hasFutureReservations(d, drive)){  //nije uracunato vrijeme trenutno radno vrijeme
                return d;
            }
            drivers.remove(d);
            if (drivers.size() == 0){throw new DriverNotAvailableException();}
        }

    }

    public Driver finishDrive(Drive drive) {
        Driver driver = drive.getDriver();
        driver.setAvailable(true);
        driver.setPosition(drive.getRoutes().get(drive.getRoutes().size() - 1).getEndPosition());
        save(driver);
        return driver;
    }

    public Driver startDrive(Drive drive) {
        Driver driver = drive.getDriver();
        driver.setAvailable(false);
        driver.setPosition(drive.getRoutes().get(0).getStartPosition());
        save(driver);
        return driver;
    }

    public List<Driver> getAll() {
        return driverRepository.findAll();
    }

    public void addDriverActivityForUnactiveDriver(Driver d) {
        DriverActivity activity = new DriverActivity();
        activity.setStartTime(LocalDateTime.now().minusHours(10));
        activity.setEndTime(LocalDateTime.now().minusHours(1));
        activity.setDriver(d);
        driverActivityRepository.save(activity);
    }
}

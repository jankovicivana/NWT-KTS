package nvt.kts.project.service;

import nvt.kts.project.dto.CarDTO;
import nvt.kts.project.exception.DriverNotAvailableException;
import nvt.kts.project.model.*;
import nvt.kts.project.repository.DriverActivityRepository;
import nvt.kts.project.repository.DriverRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.*;

public class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverActivityRepository driverActivityRepository;

    @Mock
    private DriveService driveService;


    @InjectMocks
    private DriverService driverService;

    private CarDTO carDTO;

    private Driver driver;

    private Driver driver1;

    private Car car;

    private Drive drive;

    private Drive drive1;

    private DriverActivity driverActivity;

    private DriverActivity driverActivity1;

    @BeforeMethod
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        this.carDTO = new CarDTO();
        carDTO.setPetFriendly(true);
        carDTO.setBabiesAllowed(true);
        carDTO.setType("Van XL");

        this.driver = new Driver();
        this.driver.setActive(true);
        this.driver.setAvailable(true);
        this.driver.setEnabled(true);
        this.driver.setBlocked(false);
        this.driver.setPhoto("");
        this.driver.setId(1L);
        this.driver.setEmail("email@gmail.com");

        this.driver1 = new Driver();
        this.driver1.setActive(true);
        this.driver1.setAvailable(true);
        this.driver1.setEnabled(true);
        this.driver1.setBlocked(false);
        this.driver1.setPhoto("");
        this.driver1.setId(2L);
        this.driver1.setEmail("ivana@gmail.com");

        CarType carType = new CarType();
        carType.setType("Van XL");
        carType.setPersonNum(8);
        carType.setPrice(20);

        this.car = new Car();
        this.car.setType(carType);
        this.car.setPetFriendly(true);
        this.car.setBabiesAllowed(true);

        this.driver.setCar(this.car);
        this.driver1.setCar(this.car);

        this.driverActivity = new DriverActivity();
        driverActivity.setDriver(this.driver);
        driverActivity.setStartTime(LocalDateTime.now().minusHours(2));
        driverActivity.setEndTime(LocalDateTime.now().minusHours(1));
        driverActivity.setId(1L);

        this.driverActivity1 = new DriverActivity();
        driverActivity1.setDriver(this.driver1);
        driverActivity1.setStartTime(LocalDateTime.now().minusHours(3));
        driverActivity1.setEndTime(LocalDateTime.now().minusHours(1));
        driverActivity1.setId(2L);

        this.drive = new Drive();
        drive.setStatus(DriveStatus.SCHEDULING_IN_PROGRESS);
        drive.setId(1L);
        drive.setPetFriendly(true);
        drive.setBabiesAllowed(true);
        drive.setCarType(carType);

        this.drive1 = new Drive();
        drive1.setStatus(DriveStatus.SCHEDULING_IN_PROGRESS);
        drive1.setId(1L);
        drive1.setPetFriendly(true);
        drive1.setBabiesAllowed(true);
        drive1.setCarType(carType);
        drive1.setDriver(driver1);

        Route route = new Route();

        Position startPosition = new Position();
        startPosition.setLat(45.2484513);
        startPosition.setLon(19.8487313);
        startPosition.setAddress("Puskinova 16");
        route.setStartPosition(startPosition);

        Position endPosition = new Position();
        endPosition.setLat(45.2451945);
        endPosition.setLon(19.8324524);
        endPosition.setAddress("Strazilovska 14");
        route.setEndPosition(endPosition);
        List<Route> routes = new ArrayList<>();
        routes.add(route);
        drive.setRoutes(routes);
        this.driver.setPosition(startPosition);
        this.driver1.setPosition(endPosition);
    }

    @Test
    public void shouldFindActiveAndAvailableNearestDriver(){
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        List<DriverActivity> driverActivities = new ArrayList<>();
        driverActivities.add(driverActivity);

        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(drivers);
        Mockito.when(driverActivityRepository.getDriverActivites(1L)).thenReturn(driverActivities);
        Mockito.when(driveService.hasFutureReservations(driver,drive)).thenReturn(false);
        Mockito.when(driveService.hasFutureReservations(driver1,drive)).thenReturn(false);

        Driver d = driverService.findAvailableDriver(drive);

        verify(driverRepository,times(1)).findActiveAndAvailableDriversByCarCriteria("Van XL",true,true);
        verify(driveService,times(1)).hasFutureReservations(driver,drive);

        assertTrue(d.isActive());
        assertTrue(d.isAvailable());
        assertEquals(Optional.ofNullable(d.getId()),Optional.of(1L));

    }

    @Test
    public void shouldFindActiveAndAvailableNearestDriverTwoDriverOption(){
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        drivers.add(driver1);
        List<DriverActivity> driverActivities = new ArrayList<>();
        driverActivities.add(driverActivity);
        List<DriverActivity> driverActivities1 = new ArrayList<>();
        driverActivities1.add(driverActivity1);

        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(drivers);
        Mockito.when(driverActivityRepository.getDriverActivites(1L)).thenReturn(driverActivities);
        Mockito.when(driverActivityRepository.getDriverActivites(2L)).thenReturn(driverActivities1);
        Mockito.when(driveService.hasFutureReservations(driver,drive)).thenReturn(false);
        Mockito.when(driveService.hasFutureReservations(driver1,drive)).thenReturn(false);

        Driver d = driverService.findAvailableDriver(drive);

        verify(driverRepository,times(1)).findActiveAndAvailableDriversByCarCriteria("Van XL",true,true);
        verify(driveService,times(1)).hasFutureReservations(driver,drive);

        assertTrue(d.isActive());
        assertTrue(d.isAvailable());
        assertEquals(Optional.of(d.getId()),Optional.of(1L));

    }

    @Test
    public void shouldNotChooseDriverWithMoreThan8WorkingHours(){
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        drivers.add(driver1);
        List<DriverActivity> driverActivities = new ArrayList<>();
        driverActivity.setStartTime(LocalDateTime.now().minusHours(10));
        driverActivity.setEndTime(LocalDateTime.now().minusHours(1));
        driverActivities.add(driverActivity);
        List<DriverActivity> driverActivities1 = new ArrayList<>();
        driverActivities1.add(driverActivity1);

        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(drivers);
        Mockito.when(driverActivityRepository.getDriverActivites(1L)).thenReturn(driverActivities);
        Mockito.when(driverActivityRepository.getDriverActivites(2L)).thenReturn(driverActivities1);
        Mockito.when(driveService.hasFutureReservations(driver1,drive)).thenReturn(false);

        Driver d = driverService.findAvailableDriver(drive);

        verify(driverRepository,times(1)).findActiveAndAvailableDriversByCarCriteria("Van XL",true,true);
        verify(driveService,times(1)).hasFutureReservations(driver1,drive);

        assertTrue(d.isActive());
        assertTrue(d.isAvailable());
        assertEquals(Optional.of(d.getId()),Optional.of(2L));

    }

    @Test
    public void allDriversHaveMoreThan8WorkingHours(){
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        drivers.add(driver1);
        List<DriverActivity> driverActivities = new ArrayList<>();
        driverActivity.setStartTime(LocalDateTime.now().minusHours(10));
        driverActivity.setEndTime(LocalDateTime.now().minusHours(1));
        driverActivity1.setStartTime(LocalDateTime.now().minusHours(10));
        driverActivity1.setEndTime(LocalDateTime.now().minusHours(1));
        driverActivities.add(driverActivity);
        List<DriverActivity> driverActivities1 = new ArrayList<>();
        driverActivities1.add(driverActivity1);

        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(drivers);
        Mockito.when(driverActivityRepository.getDriverActivites(1L)).thenReturn(driverActivities);
        Mockito.when(driverActivityRepository.getDriverActivites(2L)).thenReturn(driverActivities1);

        assertThrows(DriverNotAvailableException.class,()->driverService.findAvailableDriver(drive));

        verify(driverRepository,times(1)).findActiveAndAvailableDriversByCarCriteria("Van XL",true,true);
        verify(driveService,times(0)).hasFutureReservations(Mockito.any(Driver.class),Mockito.any(Drive.class));
    }


    @Test
    public void chooseDriverWithoutFutureReservations(){
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        drivers.add(driver1);
        List<DriverActivity> driverActivities = new ArrayList<>();
        driverActivities.add(driverActivity);
        List<DriverActivity> driverActivities1 = new ArrayList<>();
        driverActivities1.add(driverActivity1);

        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(drivers);
        Mockito.when(driverActivityRepository.getDriverActivites(1L)).thenReturn(driverActivities);
        Mockito.when(driverActivityRepository.getDriverActivites(2L)).thenReturn(driverActivities1);
        Mockito.when(driveService.hasFutureReservations(driver,drive)).thenReturn(true);
        Mockito.when(driveService.hasFutureReservations(driver1,drive)).thenReturn(false);

        Driver d = driverService.findAvailableDriver(drive);

        verify(driverRepository,times(1)).findActiveAndAvailableDriversByCarCriteria("Van XL",true,true);
        verify(driveService,times(2)).hasFutureReservations(Mockito.any(Driver.class),Mockito.any(Drive.class));

        assertTrue(d.isActive());
        assertTrue(d.isAvailable());
        assertEquals(Optional.of(d.getId()),Optional.of(2L));
    }

    @Test
    public void allDriversHaveFutureReservations(){
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        drivers.add(driver1);
        List<DriverActivity> driverActivities = new ArrayList<>();
        driverActivities.add(driverActivity);
        List<DriverActivity> driverActivities1 = new ArrayList<>();
        driverActivities1.add(driverActivity1);

        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(drivers);
        Mockito.when(driverActivityRepository.getDriverActivites(1L)).thenReturn(driverActivities);
        Mockito.when(driverActivityRepository.getDriverActivites(2L)).thenReturn(driverActivities1);
        Mockito.when(driveService.hasFutureReservations(driver,drive)).thenReturn(true);
        Mockito.when(driveService.hasFutureReservations(driver1,drive)).thenReturn(true);

        assertThrows(DriverNotAvailableException.class,()->driverService.findAvailableDriver(drive));

        verify(driverRepository,times(1)).findActiveAndAvailableDriversByCarCriteria("Van XL",true,true);
        verify(driveService,times(2)).hasFutureReservations(Mockito.any(Driver.class),Mockito.any(Drive.class));

    }

    @Test
    public void noActiveDriversWithWantedCarCriteria(){
        List<Driver> drivers = new ArrayList<>();
        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(drivers);
        Mockito.when(driverRepository.findActiveDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(drivers);

        assertThrows(DriverNotAvailableException.class,()->driverService.findAvailableDriver(drive));

        verify(driverRepository,times(1)).findActiveDriversByCarCriteria("Van XL",true,true);
        verify(driverActivityRepository,times(0)).getDriverActivites(1L);
        verify(driveService,times(0)).hasFutureReservations(driver,drive);

    }


    @Test
    public void shouldFindActiveDriverNearestToEnd(){
        List<Driver> activeAndAvailableDrivers = new ArrayList<>();
        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(activeAndAvailableDrivers);

        List<Driver> activeDrivers = new ArrayList<>();
        driver.setActive(true);
        driver.setAvailable(false);
        driver1.setActive(true);
        driver1.setAvailable(false);
        activeDrivers.add(driver);
        activeDrivers.add(driver1);
        drive.setStartTime(LocalDateTime.now());
        drive.setDuration(50);
        drive1.setStartTime(LocalDateTime.now());
        drive1.setDuration(30);

        Mockito.when(driverRepository.findActiveDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(activeDrivers);
        Mockito.when(driveService.getDriverCurrentDrive("email@gmail.com")).thenReturn(Collections.singletonList(drive));
        Mockito.when(driveService.getDriverCurrentDrive("ivana@gmail.com")).thenReturn(Collections.singletonList(drive1));

        Driver d = driverService.findAvailableDriver(drive);

        verify(driverRepository,times(1)).findActiveDriversByCarCriteria("Van XL",true,true);
        verify(driverActivityRepository,times(1)).getDriverActivites(2L);
        verify(driveService,times(1)).hasFutureReservations(driver1,drive);


        assertTrue(d.isActive());
        assertFalse(d.isAvailable());
        assertEquals(Optional.of(d.getId()),Optional.of(2L));
    }

    @Test
    public void shouldFindActiveDriverNearestToEndWithAvailableWorkingHours(){
        List<Driver> activeAndAvailableDrivers = new ArrayList<>();
        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(activeAndAvailableDrivers);

        List<Driver> activeDrivers = new ArrayList<>();
        driver.setActive(true);
        driver.setAvailable(false);
        driver1.setActive(true);
        driver1.setAvailable(false);
        activeDrivers.add(driver);
        activeDrivers.add(driver1);
        drive.setStartTime(LocalDateTime.now());
        drive.setDuration(50);
        drive1.setStartTime(LocalDateTime.now());
        drive1.setDuration(30);
        List<DriverActivity> driverActivities = new ArrayList<>();
        driverActivity1.setStartTime(LocalDateTime.now().minusHours(10));
        driverActivity1.setEndTime(LocalDateTime.now().minusHours(1));
        driverActivities.add(driverActivity);
        List<DriverActivity> driverActivities1 = new ArrayList<>();
        driverActivities1.add(driverActivity1);


        Mockito.when(driverRepository.findActiveDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(activeDrivers);
        Mockito.when(driveService.getDriverCurrentDrive("email@gmail.com")).thenReturn(Collections.singletonList(drive));
        Mockito.when(driveService.getDriverCurrentDrive("ivana@gmail.com")).thenReturn(Collections.singletonList(drive1));
        Mockito.when(driverActivityRepository.getDriverActivites(1L)).thenReturn(driverActivities);
        Mockito.when(driverActivityRepository.getDriverActivites(2L)).thenReturn(driverActivities1);
        Mockito.when(driveService.hasFutureReservations(driver,drive)).thenReturn(false);
        Mockito.when(driveService.hasFutureReservations(driver1,drive)).thenReturn(false);

        Driver d = driverService.findAvailableDriver(drive);

        verify(driverRepository,times(1)).findActiveDriversByCarCriteria("Van XL",true,true);
        verify(driverActivityRepository,times(1)).getDriverActivites(2L);
        verify(driverActivityRepository,times(1)).getDriverActivites(1L);
        verify(driveService,times(1)).hasFutureReservations(driver,drive);

        assertTrue(d.isActive());
        assertFalse(d.isAvailable());
        assertEquals(Optional.of(d.getId()),Optional.of(1L));
    }

    @Test
    public void noActiveDriversHaveAvailableWorkingHours(){
        List<Driver> activeAndAvailableDrivers = new ArrayList<>();
        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(activeAndAvailableDrivers);

        List<Driver> activeDrivers = new ArrayList<>();
        driver.setActive(true);
        driver.setAvailable(false);
        driver1.setActive(true);
        driver1.setAvailable(false);
        activeDrivers.add(driver);
        activeDrivers.add(driver1);
        drive.setStartTime(LocalDateTime.now());
        drive.setDuration(50);
        drive1.setStartTime(LocalDateTime.now());
        drive1.setDuration(30);
        List<DriverActivity> driverActivities = new ArrayList<>();
        driverActivity1.setStartTime(LocalDateTime.now().minusHours(10));
        driverActivity1.setEndTime(LocalDateTime.now().minusHours(1));
        driverActivity.setStartTime(LocalDateTime.now().minusHours(10));
        driverActivity.setEndTime(LocalDateTime.now().minusHours(1));
        driverActivities.add(driverActivity);
        List<DriverActivity> driverActivities1 = new ArrayList<>();
        driverActivities1.add(driverActivity1);


        Mockito.when(driverRepository.findActiveDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(activeDrivers);
        Mockito.when(driveService.getDriverCurrentDrive("email@gmail.com")).thenReturn(Collections.singletonList(drive));
        Mockito.when(driveService.getDriverCurrentDrive("ivana@gmail.com")).thenReturn(Collections.singletonList(drive1));
        Mockito.when(driverActivityRepository.getDriverActivites(1L)).thenReturn(driverActivities);
        Mockito.when(driverActivityRepository.getDriverActivites(2L)).thenReturn(driverActivities1);
        Mockito.when(driveService.hasFutureReservations(driver,drive)).thenReturn(false);
        Mockito.when(driveService.hasFutureReservations(driver1,drive)).thenReturn(false);

        assertThrows(DriverNotAvailableException.class,()->driverService.findAvailableDriver(drive));

        verify(driverRepository,times(1)).findActiveDriversByCarCriteria("Van XL",true,true);
        verify(driverActivityRepository,times(1)).getDriverActivites(2L);
        verify(driverActivityRepository,times(1)).getDriverActivites(1L);
        verify(driveService,times(0)).hasFutureReservations(Mockito.any(Driver.class),Mockito.any(Drive.class));

    }

    @Test
    public void chooseActiveDriversWithNoFutureReservations(){
        List<Driver> activeAndAvailableDrivers = new ArrayList<>();
        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(activeAndAvailableDrivers);

        List<Driver> activeDrivers = new ArrayList<>();
        driver.setActive(true);
        driver.setAvailable(false);
        driver1.setActive(true);
        driver1.setAvailable(false);
        activeDrivers.add(driver);
        activeDrivers.add(driver1);
        drive.setStartTime(LocalDateTime.now());
        drive.setDuration(50);
        drive1.setStartTime(LocalDateTime.now());
        drive1.setDuration(30);
        List<DriverActivity> driverActivities = new ArrayList<>();
        List<DriverActivity> driverActivities1 = new ArrayList<>();
        driverActivities1.add(driverActivity1);


        Mockito.when(driverRepository.findActiveDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(activeDrivers);
        Mockito.when(driveService.getDriverCurrentDrive("email@gmail.com")).thenReturn(Collections.singletonList(drive));
        Mockito.when(driveService.getDriverCurrentDrive("ivana@gmail.com")).thenReturn(Collections.singletonList(drive1));
        Mockito.when(driverActivityRepository.getDriverActivites(1L)).thenReturn(driverActivities);
        Mockito.when(driverActivityRepository.getDriverActivites(2L)).thenReturn(driverActivities1);
        Mockito.when(driveService.hasFutureReservations(driver,drive)).thenReturn(true);
        Mockito.when(driveService.hasFutureReservations(driver1,drive)).thenReturn(false);

        Driver d = driverService.findAvailableDriver(drive);

        verify(driverRepository,times(1)).findActiveDriversByCarCriteria("Van XL",true,true);
        verify(driverActivityRepository,times(1)).getDriverActivites(2L);
        verify(driveService,times(1)).hasFutureReservations(Mockito.any(Driver.class),Mockito.any(Drive.class));

        assertEquals(Optional.of(d.getId()),Optional.of(2L));
    }

    @Test
    public void allActiveDriversHaveFutureReservations(){
        List<Driver> activeAndAvailableDrivers = new ArrayList<>();
        Mockito.when(driverRepository.findActiveAndAvailableDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(activeAndAvailableDrivers);

        List<Driver> activeDrivers = new ArrayList<>();
        driver.setActive(true);
        driver.setAvailable(false);
        driver1.setActive(true);
        driver1.setAvailable(false);
        activeDrivers.add(driver);
        activeDrivers.add(driver1);
        drive.setStartTime(LocalDateTime.now());
        drive.setDuration(50);
        drive1.setStartTime(LocalDateTime.now());
        drive1.setDuration(30);
        List<DriverActivity> driverActivities = new ArrayList<>();
        List<DriverActivity> driverActivities1 = new ArrayList<>();

        Mockito.when(driverRepository.findActiveDriversByCarCriteria(carDTO.getType(),carDTO.isBabiesAllowed(), carDTO.isPetFriendly())).thenReturn(activeDrivers);
        Mockito.when(driveService.getDriverCurrentDrive("email@gmail.com")).thenReturn(Collections.singletonList(drive));
        Mockito.when(driveService.getDriverCurrentDrive("ivana@gmail.com")).thenReturn(Collections.singletonList(drive1));
        Mockito.when(driverActivityRepository.getDriverActivites(1L)).thenReturn(driverActivities);
        Mockito.when(driverActivityRepository.getDriverActivites(2L)).thenReturn(driverActivities1);
        Mockito.when(driveService.hasFutureReservations(driver,drive)).thenReturn(true);
        Mockito.when(driveService.hasFutureReservations(driver1,drive)).thenReturn(true);

        assertThrows(DriverNotAvailableException.class,()->driverService.findAvailableDriver(drive));

        verify(driverRepository,times(1)).findActiveDriversByCarCriteria("Van XL",true,true);
        verify(driverActivityRepository,times(1)).getDriverActivites(2L);
        verify(driverActivityRepository,times(1)).getDriverActivites(1L);
        verify(driveService,times(2)).hasFutureReservations(Mockito.any(Driver.class),Mockito.any(Drive.class));
    }
}

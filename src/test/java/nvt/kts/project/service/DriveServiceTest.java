package nvt.kts.project.service;

import nvt.kts.project.dto.PositionDTO;
import nvt.kts.project.dto.RouteDTO;
import nvt.kts.project.dto.ScheduleInfoDTO;
import nvt.kts.project.model.*;
import nvt.kts.project.repository.ClientDriveRepository;
import nvt.kts.project.repository.DriveRepository;
import org.mockito.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.*;

public class DriveServiceTest {

    @Mock
    private DriveRepository driveRepository;

    @Mock
    private CarService carService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ClientDriveRepository clientDriveRepository;

    @Mock
    private RouteService routeService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private DriveService driveService;

    private ScheduleInfoDTO info;

    private Client client;

    private CarType carType;

    private List<RouteDTO> routes;

    private Drive drive;

    @Captor
    private ArgumentCaptor<Drive> driveArgumentCaptor;

    @Captor
    private ArgumentCaptor<ClientDrive> clientDriveArgumentCaptor;

    @Captor
    private ArgumentCaptor<Route> routeArgumentCaptor;

    @Captor
    private ArgumentCaptor<Reservation> reservationArgumentCaptor;

    @BeforeMethod
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        this.info = new ScheduleInfoDTO();
        info.setCar("Van XL");
        info.setBabies(false);
        info.setPet(false);
        info.setPrice(20.0);
        info.setSplitFaire(false);
        info.setReservation(false);
        info.setDistance(3.0);
        info.setDuration(3.0);
        info.setPassengers(new ArrayList<>());

        this.drive = new Drive();
        drive.setStatus(DriveStatus.SCHEDULING_IN_PROGRESS);
        drive.setId(1L);

        RouteDTO r = new RouteDTO();
        PositionDTO start = new PositionDTO();
        start.setLon(40.0);
        start.setLat(19.0);
        start.setAddress("Strazilovska 14");

        PositionDTO end = new PositionDTO();
        end.setLon(30.0);
        end.setLat(19.0);
        end.setAddress("Puskinova 16");

        r.setType("recommended");
        r.setStart(start);
        r.setEnd(end);

        this.routes = new ArrayList<>();
        routes.add(r);
        info.setRoutes(routes);

        info.setReservationTime(null);
        info.setFavourite(false);

        this.client = new Client();
        client.setId(1L);
        client.setTokens(0);
        client.setPhoto("");
        client.setCity("Novi Sad");
        client.setEmail("client@gmail.com");
        client.setPassword("pass");
        client.setDriving(false);
        client.setBlocked(false);
        client.setDeleted(false);

        this.carType = new CarType();
        carType.setType("Van XL");
        carType.setId(1L);
        carType.setPersonNum(8);
        carType.setPrice(20.0);
    }

    @Test
    public void shouldSaveDriveNoReservationOneClient(){

        Mockito.when(carService.findCarTypeByName("Van XL")).thenReturn(carType);

        Drive drive = driveService.saveDrive(info, client);

        verify(driveRepository, times(1)).save(driveArgumentCaptor.capture());
        verify(clientDriveRepository, times(1)).save(clientDriveArgumentCaptor.capture());
        verify(routeService, times(1)).save(routeArgumentCaptor.capture());

        assertEquals(driveArgumentCaptor.getValue().getStatus(), DriveStatus.SCHEDULING_IN_PROGRESS);
        assertNull(drive.getStartTime());
        assertEquals(drive.getRoutes().size(), 1);
        assertEquals(drive.getCarType().getType(), "Van XL");
    }

    @Test
    public void shouldSaveDriveReservationOneClient(){

        LocalDateTime time = LocalDateTime.now().plusHours(1);
        String timeString = time.toString();

        info.setReservation(true);
        info.setReservationTime(timeString);

        Reservation res = new Reservation();
        res.setDrive(null);
        res.setStart(time);
        res.setExpectedDuration(3.0);
        res.setId(1L);

        Mockito.when(carService.findCarTypeByName("Van XL")).thenReturn(carType);
        Mockito.when(reservationService.save(reservationArgumentCaptor.capture())).thenReturn(res);

        Drive drive = driveService.saveDrive(info, client);

        verify(driveRepository, times(2)).save(driveArgumentCaptor.capture());
        verify(clientDriveRepository, times(1)).save(clientDriveArgumentCaptor.capture());
        verify(routeService, times(1)).save(routeArgumentCaptor.capture());
        verify(reservationService, times(1)).save(reservationArgumentCaptor.capture());

        assertEquals(driveArgumentCaptor.getValue().getStatus(), DriveStatus.RESERVED);
        assertNull(drive.getStartTime());
        assertEquals(drive.getRoutes().size(), 1);
        assertEquals(drive.getCarType().getType(), "Van XL");
        assertNotNull(drive.getReservation());
        assertEquals(drive.getReservation().getStart(), time);
    }

    @Test
    public void shouldSaveDriveReservationClientAndPassenger(){

        LocalDateTime time = LocalDateTime.now().plusHours(1);
        String timeString = time.toString();

        info.setReservation(true);
        info.setReservationTime(timeString);
        info.getPassengers().add("ivana@gmail.com");

        Client passenger = new Client();
        passenger.setId(2L);
        passenger.setTokens(0);
        passenger.setPhoto("");
        passenger.setCity("Novi Sad");
        passenger.setEmail("ivana@gmail.com");
        passenger.setPassword("pass");
        passenger.setDriving(false);
        passenger.setBlocked(false);
        passenger.setDeleted(false);

        Reservation res = new Reservation();
        res.setDrive(null);
        res.setStart(time);
        res.setExpectedDuration(3.0);
        res.setId(1L);

        Mockito.when(carService.findCarTypeByName("Van XL")).thenReturn(carType);
        Mockito.when(reservationService.save(reservationArgumentCaptor.capture())).thenReturn(res);
        Mockito.when(clientService.getClientByEmail("ivana@gmail.com")).thenReturn(passenger);

        Drive drive = driveService.saveDrive(info, client);

        verify(driveRepository, times(2)).save(driveArgumentCaptor.capture());
        verify(clientDriveRepository, times(2)).save(clientDriveArgumentCaptor.capture());
        verify(routeService, times(1)).save(routeArgumentCaptor.capture());
        verify(reservationService, times(1)).save(reservationArgumentCaptor.capture());

        assertEquals(driveArgumentCaptor.getValue().getStatus(), DriveStatus.RESERVED);
        assertNull(drive.getStartTime());
        assertEquals(drive.getRoutes().size(), 1);
        assertEquals(drive.getCarType().getType(), "Van XL");
        assertNotNull(drive.getReservation());
        assertEquals(drive.getReservation().getStart(), time);
        assertEquals(clientDriveArgumentCaptor.getAllValues().get(0).getClient().getEmail(), "client@gmail.com");
        assertEquals(clientDriveArgumentCaptor.getAllValues().get(1).getClient().getEmail(), "ivana@gmail.com");
    }


    @Test
    public void shouldSaveDriveWithReservationClientAndPassengerTwoRoutes(){

        LocalDateTime time = LocalDateTime.now().plusHours(1);
        String timeString = time.toString();

        RouteDTO r = new RouteDTO();
        PositionDTO start = new PositionDTO();
        start.setLon(30.0);
        start.setLat(19.0);
        start.setAddress("Puskinova 16");

        PositionDTO end = new PositionDTO();
        end.setLon(50.0);
        end.setLat(39.0);
        end.setAddress("Ljermontova 6");

        r.setType("fastest");
        r.setStart(start);
        r.setEnd(end);

        info.setReservation(true);
        info.setReservationTime(timeString);
        info.getPassengers().add("ivana@gmail.com");
        info.getRoutes().add(r);

        Client passenger = new Client();
        passenger.setId(2L);
        passenger.setTokens(0);
        passenger.setPhoto("");
        passenger.setCity("Novi Sad");
        passenger.setEmail("ivana@gmail.com");
        passenger.setPassword("pass");
        passenger.setDriving(false);
        passenger.setBlocked(false);
        passenger.setDeleted(false);

        Reservation res = new Reservation();
        res.setDrive(null);
        res.setStart(time);
        res.setExpectedDuration(3.0);
        res.setId(1L);

        Mockito.when(carService.findCarTypeByName("Van XL")).thenReturn(carType);
        Mockito.when(reservationService.save(reservationArgumentCaptor.capture())).thenReturn(res);
        Mockito.when(clientService.getClientByEmail("ivana@gmail.com")).thenReturn(passenger);

        Drive drive = driveService.saveDrive(info, client);

        verify(driveRepository, times(2)).save(driveArgumentCaptor.capture());
        verify(clientDriveRepository, times(2)).save(clientDriveArgumentCaptor.capture());
        verify(routeService, times(2)).save(routeArgumentCaptor.capture());
        verify(reservationService, times(1)).save(reservationArgumentCaptor.capture());

        assertEquals(driveArgumentCaptor.getValue().getStatus(), DriveStatus.RESERVED);
        assertNull(drive.getStartTime());
        assertEquals(drive.getRoutes().size(), 2);
        assertEquals(drive.getCarType().getType(), "Van XL");
        assertNotNull(drive.getReservation());
        assertEquals(drive.getReservation().getStart(), time);
        assertEquals(clientDriveArgumentCaptor.getAllValues().get(0).getClient().getEmail(), "client@gmail.com");
        assertEquals(clientDriveArgumentCaptor.getAllValues().get(1).getClient().getEmail(), "ivana@gmail.com");
    }

    @Test
    public void shouldSaveDriveNoReservationClientAndPassengerTwoRoutes(){

        RouteDTO r = new RouteDTO();
        PositionDTO start = new PositionDTO();
        start.setLon(30.0);
        start.setLat(19.0);
        start.setAddress("Puskinova 16");

        PositionDTO end = new PositionDTO();
        end.setLon(50.0);
        end.setLat(39.0);
        end.setAddress("Ljermontova 6");

        r.setType("fastest");
        r.setStart(start);
        r.setEnd(end);

        info.getPassengers().add("ivana@gmail.com");
        info.getRoutes().add(r);

        Client passenger = new Client();
        passenger.setId(2L);
        passenger.setTokens(0);
        passenger.setPhoto("");
        passenger.setCity("Novi Sad");
        passenger.setEmail("ivana@gmail.com");
        passenger.setPassword("pass");
        passenger.setDriving(false);
        passenger.setBlocked(false);
        passenger.setDeleted(false);

        Mockito.when(carService.findCarTypeByName("Van XL")).thenReturn(carType);
        Mockito.when(clientService.getClientByEmail("ivana@gmail.com")).thenReturn(passenger);

        Drive drive = driveService.saveDrive(info, client);

        verify(driveRepository, times(1)).save(driveArgumentCaptor.capture());
        verify(clientDriveRepository, times(2)).save(clientDriveArgumentCaptor.capture());
        verify(routeService, times(2)).save(routeArgumentCaptor.capture());
        verify(reservationService, times(0)).save(reservationArgumentCaptor.capture());

        assertEquals(driveArgumentCaptor.getValue().getStatus(), DriveStatus.SCHEDULING_IN_PROGRESS);
        assertNull(drive.getStartTime());
        assertEquals(drive.getRoutes().size(), 2);
        assertEquals(drive.getCarType().getType(), "Van XL");
        assertNull(drive.getReservation());
        assertEquals(clientDriveArgumentCaptor.getAllValues().get(0).getClient().getEmail(), "client@gmail.com");
        assertEquals(clientDriveArgumentCaptor.getAllValues().get(1).getClient().getEmail(), "ivana@gmail.com");
    }


    @Test
    public void shouldSaveRoute(){
        driveService.saveRoutes(routes, drive);
        verify(routeService, times(1)).save(Mockito.any(Route.class));
    }

    @Test
    public void shouldSaveTwoRoutes(){
        RouteDTO r = new RouteDTO();
        PositionDTO start = new PositionDTO();
        start.setLon(30.0);
        start.setLat(19.0);
        start.setAddress("Puskinova 16");

        PositionDTO end = new PositionDTO();
        end.setLon(50.0);
        end.setLat(39.0);
        end.setAddress("Ljermontova 6");

        r.setType("fastest");
        r.setStart(start);
        r.setEnd(end);

        routes.add(r);

        driveService.saveRoutes(routes, drive);
        verify(routeService, times(2)).save(Mockito.any(Route.class));
    }

    @Test
    public void shouldCreateClientDriveOneClient(){
        ClientDrive cd = driveService.createClientDrive(drive, client, info, true, false);

        assertFalse(cd.isFavourite());
        assertEquals(cd.getPrice(), 20.0);
        assertEquals(cd.getClient().getEmail(), client.getEmail());
    }

    @Test
    public void shouldCreateClientDriveTwoClientsNotSplitFaire(){

        Client passenger = new Client();
        passenger.setId(2L);
        passenger.setTokens(0);
        passenger.setPhoto("");
        passenger.setCity("Novi Sad");
        passenger.setEmail("ivana@gmail.com");
        passenger.setPassword("pass");
        passenger.setDriving(false);
        passenger.setBlocked(false);
        passenger.setDeleted(false);
        info.getPassengers().add("ivana@gmail.com");

        ClientDrive cd = driveService.createClientDrive(drive, client, info, true, false);

        assertFalse(cd.isFavourite());
        assertEquals(cd.getPrice(), 20.0);
        assertEquals(cd.getClient().getEmail(), client.getEmail());

        ClientDrive cd1 = driveService.createClientDrive(drive, passenger, info, false, false);

        assertFalse(cd1.isFavourite());
        assertEquals(cd1.getPrice(), 0.0);
        assertEquals(cd1.getClient().getEmail(), passenger.getEmail());
    }

    @Test
    public void shouldCreateClientDriveTwoClientsSplitFaire(){

        Client passenger = new Client();
        passenger.setId(2L);
        passenger.setTokens(0);
        passenger.setPhoto("");
        passenger.setCity("Novi Sad");
        passenger.setEmail("ivana@gmail.com");
        passenger.setPassword("pass");
        passenger.setDriving(false);
        passenger.setBlocked(false);
        passenger.setDeleted(false);
        info.getPassengers().add("ivana@gmail.com");
        info.setSplitFaire(true);

        ClientDrive cd = driveService.createClientDrive(drive, client, info, true, false);

        assertFalse(cd.isFavourite());
        assertEquals(cd.getPrice(), 10.0);
        assertEquals(cd.getClient().getEmail(), client.getEmail());

        ClientDrive cd1 = driveService.createClientDrive(drive, passenger, info, false, false);

        assertFalse(cd1.isFavourite());
        assertEquals(cd1.getPrice(), 10.0);
        assertEquals(cd1.getClient().getEmail(), passenger.getEmail());
    }

}

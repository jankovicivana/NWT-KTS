package nvt.kts.project.service;

import javafx.geometry.Pos;
import nvt.kts.project.dto.DriveDTO;
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
import java.util.Optional;

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
    private SystemInfoService systemInfoService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private DriveService driveService;

    private ScheduleInfoDTO info;

    private Client client;

    private CarType carType;

    private ClientDrive clientDrive;

    private ClientDrive clientDrive2;

    private List<RouteDTO> routes;

    private List<Route> routesList;

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

        this.clientDrive = new ClientDrive();
        clientDrive.setClient(client);
        clientDrive.setFavourite(false);
        clientDrive.setId(1L);
        clientDrive.setApproved(false);
        clientDrive.setDrive(drive);

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

        this.clientDrive2 = new ClientDrive();
        clientDrive2.setClient(passenger);
        clientDrive2.setFavourite(false);
        clientDrive2.setId(2L);
        clientDrive2.setApproved(false);

        Route route = new Route();
        Position startPosition = new Position();
        startPosition.setLon(40.0);
        startPosition.setLat(19.0);
        startPosition.setAddress("Strazilovska 14");

        Position endPosition = new Position();
        endPosition.setLon(30.0);
        endPosition.setLat(19.0);
        endPosition.setAddress("Puskinova 16");

        route.setStartPosition(startPosition);
        route.setEndPosition(endPosition);
        r.setType("recommended");
        this.routesList = new ArrayList<>();
        routesList.add(route);
        drive.setRoutes(routesList);

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

    @Test
    public void shouldFindDriveByClientDrive(){
        Mockito.when(clientDriveRepository.findById(1L)).thenReturn(Optional.of(clientDrive));

        Drive d = driveService.findDriveByClientDrive(1L);

        verify(clientDriveRepository,times(1)).findById(1L);

        assertEquals(Optional.of(d.getId()),Optional.of(1L));

    }

    @Test
    public void shouldNotFindDriveByClientDrive(){
        Mockito.when(clientDriveRepository.findById(1L)).thenReturn(Optional.empty());

        Drive d = driveService.findDriveByClientDrive(1L);

        verify(clientDriveRepository,times(1)).findById(1L);

        assertNull(d);

    }

    @Test
    public void onePassengerNotApprovedPayment(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDrive.setApproved(false);
        clientDriveList.add(clientDrive);
        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);

        boolean approved = driveService.checkIfAllPassengersApprovedPayment(drive);

        verify(clientDriveRepository,times(1)).getClientDriveByDrive(1L);

        assertFalse(approved);

    }

    @Test
    public void onePassengerApprovedPayment(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDrive.setApproved(true);
        clientDriveList.add(clientDrive);
        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);

        boolean approved = driveService.checkIfAllPassengersApprovedPayment(drive);

        verify(clientDriveRepository,times(1)).getClientDriveByDrive(1L);

        assertTrue(approved);

    }

    @Test
    public void twoPassengersApprovedPayment(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDrive.setApproved(true);
        clientDrive2.setApproved(true);
        clientDriveList.add(clientDrive);
        clientDriveList.add(clientDrive2);
        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);

        boolean approved = driveService.checkIfAllPassengersApprovedPayment(drive);

        verify(clientDriveRepository,times(1)).getClientDriveByDrive(1L);

        assertTrue(approved);

    }

    @Test
    public void twoPassengersNotApprovedPayment(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDrive.setApproved(false);
        clientDrive2.setApproved(false);
        clientDriveList.add(clientDrive);
        clientDriveList.add(clientDrive2);
        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);

        boolean approved = driveService.checkIfAllPassengersApprovedPayment(drive);

        verify(clientDriveRepository,times(1)).getClientDriveByDrive(1L);

        assertFalse(approved);

    }

    @Test
    public void twoPassengersOneNotApprovedPayment(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDrive.setApproved(true);
        clientDrive2.setApproved(false);
        clientDriveList.add(clientDrive);
        clientDriveList.add(clientDrive2);
        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);

        boolean approved = driveService.checkIfAllPassengersApprovedPayment(drive);

        verify(clientDriveRepository,times(1)).getClientDriveByDrive(1L);

        assertFalse(approved);

    }

    @Test
    public void allPassengersCanPay(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDrive.setApproved(false);
        clientDrive.setPrice(30);
        clientDrive.getClient().setTokens(10);
        clientDrive2.setApproved(false);
        clientDrive2.getClient().setTokens(20);
        clientDrive2.setPrice(30);
        clientDriveList.add(clientDrive);
        clientDriveList.add(clientDrive2);

        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);
        Mockito.when(systemInfoService.getTokenPrice()).thenReturn(5.0);

        boolean canPay = driveService.checkIfAllCanPay(drive);

        verify(systemInfoService,times(1)).getTokenPrice();
        verify(clientDriveRepository,times(1)).getClientDriveByDrive(1L);

        assertTrue(canPay);
    }

    @Test
    public void noPassengerCanPay(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDrive.setApproved(false);
        clientDrive.setPrice(100);
        clientDrive.getClient().setTokens(10);
        clientDrive2.setApproved(false);
        clientDrive2.getClient().setTokens(10);
        clientDrive2.setPrice(100);
        clientDriveList.add(clientDrive);
        clientDriveList.add(clientDrive2);

        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);
        Mockito.when(systemInfoService.getTokenPrice()).thenReturn(5.0);

        boolean canPay = driveService.checkIfAllCanPay(drive);

        verify(systemInfoService,times(1)).getTokenPrice();
        verify(clientDriveRepository,times(1)).getClientDriveByDrive(1L);

        assertFalse(canPay);
    }

    @Test
    public void onePassengerCanNotPay(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDrive.setApproved(false);
        clientDrive.setPrice(100);
        clientDrive.getClient().setTokens(100);
        clientDrive2.setApproved(false);
        clientDrive2.getClient().setTokens(19);
        clientDrive2.setPrice(100);
        clientDriveList.add(clientDrive);
        clientDriveList.add(clientDrive2);

        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);
        Mockito.when(systemInfoService.getTokenPrice()).thenReturn(5.0);

        boolean canPay = driveService.checkIfAllCanPay(drive);

        verify(systemInfoService,times(1)).getTokenPrice();
        verify(clientDriveRepository,times(1)).getClientDriveByDrive(1L);

        assertFalse(canPay);
    }


    @Test
    public void allPayedSuccessfully(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDrive.setApproved(true);
        clientDrive.setPrice(100);
        clientDrive.getClient().setTokens(100);
        clientDrive2.setApproved(true);
        clientDrive2.getClient().setTokens(19);
        clientDrive2.setPrice(100);
        clientDriveList.add(clientDrive);
        clientDriveList.add(clientDrive2);

        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);
        Mockito.when(systemInfoService.getTokenPrice()).thenReturn(5.0);

        driveService.payDrive(drive);

        verify(clientService,times(2)).save(Mockito.any(Client.class));

    }

    @Test
    public void successfullyRejectDrive(){
        DriveDTO dto = new DriveDTO();
        dto.setId(1L);
        dto.setRejectionReason("neki razlog");
        Mockito.when(driveRepository.findById(1L)).thenReturn(Optional.ofNullable(drive));

        Drive d = driveService.rejectDrive(dto);

        verify(driveRepository,times(1)).save(Mockito.any(Drive.class));
        assertEquals(d.getStatus(),DriveStatus.REJECTED);
        assertEquals(d.getId(),drive.getId());
    }


    @Test
    public void successfullyFindDriverEmptyDrive(){
        List<Drive> emptyDrives = new ArrayList<>();
        drive.setStatus(DriveStatus.EMPTY);
        Driver driver = new Driver();
        driver.setEmail("driver@gmail.com");
        drive.setDriver(driver);
        drive.setRoutes(routesList);

        drive.getRoutes().get(0).getEndPosition().setAddress("Puskinova 16");
        emptyDrives.add(drive);
        Mockito.when(driveRepository.getDriverEmptyDrives("driver@gmail.com")).thenReturn(emptyDrives);

        Drive d = driveService.getDriverEmptyDrive("driver@gmail.com","Puskinova 16");

        verify(driveRepository,times(1)).getDriverEmptyDrives(Mockito.any(String.class));
        assertEquals(d.getRoutes().get(0).getEndPosition().getAddress(),drive.getRoutes().get(0).getEndPosition().getAddress());
    }

    @Test
    public void shouldNotFindDriverEmptyDriveWrongEmail(){
        List<Drive> emptyDrives = new ArrayList<>();
        drive.setStatus(DriveStatus.EMPTY);
        Driver driver = new Driver();
        driver.setEmail("driver@gmail.com");
        drive.setDriver(driver);
        drive.setRoutes(routesList);

        drive.getRoutes().get(0).getEndPosition().setAddress("Puskinova 16");
        emptyDrives.add(drive);
        Mockito.when(driveRepository.getDriverEmptyDrives("driver@gmail.com")).thenReturn(emptyDrives);

        //taj vozac nema prazne voznje
        Drive d = driveService.getDriverEmptyDrive("wrongEmail@gmail.com","Puskinova 16");

        verify(driveRepository,times(1)).getDriverEmptyDrives(Mockito.any(String.class));
        assertNull(d);
    }

    @Test
    public void shouldNotFindDriverEmptyDrivePositionsDoNotMatch(){
        List<Drive> emptyDrives = new ArrayList<>();
        drive.setStatus(DriveStatus.EMPTY);
        Driver driver = new Driver();
        driver.setEmail("driver@gmail.com");
        drive.setDriver(driver);
        drive.setRoutes(routesList);

        drive.getRoutes().get(0).getEndPosition().setAddress("Puskinova 16");
        emptyDrives.add(drive);
        Mockito.when(driveRepository.getDriverEmptyDrives("driver@gmail.com")).thenReturn(emptyDrives);

        //taj vozac nema odgovarajaucu  praznu voznju
        Drive d = driveService.getDriverEmptyDrive("driver@gmail.com","Strazilovska 16");

        verify(driveRepository,times(1)).getDriverEmptyDrives(Mockito.any(String.class));
        assertNull(d);
    }

}

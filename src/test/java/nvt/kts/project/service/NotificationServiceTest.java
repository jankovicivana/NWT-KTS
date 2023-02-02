package nvt.kts.project.service;

import nvt.kts.project.dto.NotificationDTO;
import nvt.kts.project.model.*;
import nvt.kts.project.repository.ClientDriveRepository;
import nvt.kts.project.repository.NotificationRepository;
import org.mockito.*;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;


public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ClientDriveRepository clientDriveRepository;

    @Mock
    private SystemInfoService systemInfoService;

    @Mock
    private RouteService routeService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private NotificationService notificationService;

    private Drive drive;

    private ClientDrive clientDrive;

    private ClientDrive clientDrive2;

    private List<Route> routes;

    @Captor
    private ArgumentCaptor<List<Notification>> notificationsCaptor;

    @BeforeMethod
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        Client client = new Client();
        client.setId(1L);
        client.setTokens(0);
        client.setPhoto("");
        client.setCity("Novi Sad");
        client.setEmail("client@gmail.com");
        client.setPassword("pass");
        client.setDriving(false);
        client.setBlocked(false);
        client.setDeleted(false);

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

        this.clientDrive = new ClientDrive();
        clientDrive.setClient(client);
        clientDrive.setFavourite(false);
        clientDrive.setId(1L);
        clientDrive.setApproved(false);

        this.clientDrive2 = new ClientDrive();
        clientDrive2.setClient(passenger);
        clientDrive2.setFavourite(false);
        clientDrive2.setId(2L);
        clientDrive2.setApproved(false);

        Route r = new Route();
        Position start = new Position();
        start.setLon(40.0);
        start.setLat(19.0);
        start.setAddress("Strazilovska 14");

        Position end = new Position();
        end.setLon(30.0);
        end.setLat(19.0);
        end.setAddress("Puskinova 16");

        r.setType("recommended");
        r.setStartPosition(start);
        r.setEndPosition(end);

        routes = new ArrayList<>();
        routes.add(r);

        this.drive = new Drive();
        drive.setStatus(DriveStatus.SCHEDULING_IN_PROGRESS);
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDriveList.add(clientDrive);
        drive.setPassengers(clientDriveList);
        drive.setId(1L);
        drive.setRoutes(routes);

    }

    @Test
    public void shouldReturnOneNotification(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDriveList.add(clientDrive);
        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);
        Mockito.when(systemInfoService.getTokenPrice()).thenReturn(20.0);
        Mockito.when(routeService.getRoutes(1L)).thenReturn(routes);

        List<Notification> notifications = notificationService.sendNotificationsForApprovingPayment(drive);

        verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/notification/approvePayment"), Mockito.any(NotificationDTO.class));
        verify(notificationRepository, times(1)).saveAll(notificationsCaptor.capture());

        assertEquals(notifications.size(), 1);
        assertEquals(notifications.get(0).getClient().getEmail(), "client@gmail.com");
        assertEquals(notifications.get(0).getReason(), NotificationReason.APPROVE_PAYMENT);
    }

    @Test
    public void shouldReturnTwoNotifications(){
        List<ClientDrive> clientDriveList = new ArrayList<>();
        clientDriveList.add(clientDrive);
        clientDriveList.add(clientDrive2);

        Mockito.when(clientDriveRepository.getClientDriveByDrive(1L)).thenReturn(clientDriveList);
        Mockito.when(systemInfoService.getTokenPrice()).thenReturn(20.0);
        Mockito.when(routeService.getRoutes(1L)).thenReturn(routes);

        List<Notification> notifications = notificationService.sendNotificationsForApprovingPayment(drive);

        verify(simpMessagingTemplate, times(2)).convertAndSend(eq("/notification/approvePayment"), Mockito.any(NotificationDTO.class));
        verify(notificationRepository, times(1)).saveAll(notificationsCaptor.capture());

        assertEquals(notifications.size(), 2);
        assertEquals(notifications.get(0).getClient().getEmail(), "client@gmail.com");
        assertEquals(notifications.get(0).getReason(), NotificationReason.APPROVE_PAYMENT);
        assertEquals(notifications.get(1).getClient().getEmail(), "ivana@gmail.com");
        assertEquals(notifications.get(1).getReason(), NotificationReason.APPROVE_PAYMENT);
    }

}

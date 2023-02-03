package nvt.kts.project.service;

import nvt.kts.project.dto.NotificationDTO;
import nvt.kts.project.model.*;
import nvt.kts.project.repository.ClientDriveRepository;
import nvt.kts.project.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ClientDriveRepository clientDriveRepository;

    @Autowired
    private SystemInfoService systemInfoService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private RouteService routeService;


    public List<Notification> sendNotificationsForApprovingPayment(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        Double tokenPrice = systemInfoService.getTokenPrice();
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.APPROVE_PAYMENT);
            n.setMessage("Molim vas odobrite placanje voznje na ruti "+route+" i iznosi "+clientDrive.getPrice()+"$" +
                    " = "+clientDrive.getPrice()/tokenPrice+ " tokena.");
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/approvePayment",dto);
        }
        notificationRepository.saveAll(newNotifications);
        return newNotifications;
    }


    public List<Notification> getNotifications(Client c) {
        return notificationRepository.findAllByClientId(c.getId());
    }

    public List<Notification> sendNotificationForRejectingDriveNotEnoughTokens(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.REJECT_DRIVE);
            n.setMessage("Voznja na ruti "+route+"je otkazana zbog nedostatka tokena.");
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/rejectedPayment",dto);
        }
        notificationRepository.saveAll(newNotifications);
        return newNotifications;
    }

    public List<Notification> sendNotificationForRejectingDriveNoAvailableDriver(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.REJECT_DRIVE);
            n.setMessage("Voznja na ruti "+route+" je otkazana.Nema dostupnih vozaca.");
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/noAvailableDriver",dto);
        }
        notificationRepository.saveAll(newNotifications);
        return newNotifications;
    }

    public List<Notification> sendNotificationForAcceptingDrive(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.DRIVE_SCHEDULED);
            n.setMessage("Voznja na ruti "+route+" je prihvacena. Automobil uskoro stize na vasu adresu.");
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/approvedDrive",dto);
        }
        //posalji i vozacu
        sendNotificationOfAcceptedDriveToDriver(d);
        notificationRepository.saveAll(newNotifications);
        return newNotifications;
    }

    private void sendNotificationOfAcceptedDriveToDriver(Drive d) {
        String route = getRouteString(d);
        Notification n = new Notification();
        n.setClient(null);
        n.setDrive(d);
        n.setReason(NotificationReason.REJECT_DRIVE);
        n.setMessage("Voznja na ruti "+route+" je prihvacena.");
        n.setDateTime(LocalDateTime.now());
        NotificationDTO dto = new NotificationDTO(n);
        dto.setReceiverEmail(n.getDrive().getDriver().getEmail());
        dto.setApprovedPayment(false);
        this.simpMessagingTemplate.convertAndSend("/notification/approvedDrive",dto);
    }

    private String getRouteString(Drive d){
        List<Route> routes = this.routeService.getRoutes(d.getId());
        return routes.get(0).getStartPosition().getAddress()+" - "+routes.get(routes.size()-1).getEndPosition().getAddress();
    }

    public void sendReservationReminder(Reservation r, int i) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(r.getDrive().getId());
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(r.getDrive());
            n.setReason(NotificationReason.DRIVE_REMINDER);
            n.setMessage("Podsjetnik: imate zakazanu voznju za " + i + " minuta na relaciji " + getRouteString(r.getDrive()));
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/reminder", dto);
        }
        notificationRepository.saveAll(newNotifications);
    }

    public List<Notification> sendNotificationForDriverRejectingDrive(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.REJECT_DRIVE);
            n.setMessage("Voznja na ruti "+route+" je otkazana.Vozac je otkazao. Razlog je: "+d.getRejectionReason());
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/driverRejected",dto);
        }
        notificationRepository.saveAll(newNotifications);
        return newNotifications;
    }

    public void sendNotificationsForStartingDrive(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.CAR_ARRIVED);
            n.setMessage("Voznja na ruti "+route+" je pocela.");
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/driveStarted",dto);
        }
        notificationRepository.saveAll(newNotifications);
    }

    public void sendNotificationsForStoppingDrive(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.STOPPED);
            n.setMessage("Voznja na ruti "+route+" je zaustavljena.");
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/driveStarted",dto);
        }
        notificationRepository.saveAll(newNotifications);
    }
    public void sendNotificationsForFinishedDrive(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.FINISHED);
            n.setMessage("Voznja na ruti "+route+" je zavrsena.");
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/driveFinished",dto);
        }
        notificationRepository.saveAll(newNotifications);
    }

    public void sendNotificationGoingToClient(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.GOING_TO_CLIENT);
            n.setMessage("Voznja na ruti "+route+" uskoro pocinje. Vozac je krenuo ka lokaciji.");
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/goingToClient",dto);
        }
        notificationRepository.saveAll(newNotifications);
    }

    public void sendNotificationTimeLeft(Drive d, long time) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.GOING_TO_CLIENT);
            n.setMessage("Voznja na ruti "+route+" uskoro pocinje. Vozac stize za " + time + " minuta.");
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/goingToClient",dto);
        }
        notificationRepository.saveAll(newNotifications);
    }
}

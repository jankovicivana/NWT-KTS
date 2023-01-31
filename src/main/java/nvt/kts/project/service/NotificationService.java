package nvt.kts.project.service;

import nvt.kts.project.dto.DriveDTO;
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
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    public void sendNotificationsForApprovingPayment(Drive d) {
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
    }


    public List<Notification> getNotifications(Client c) {
        return notificationRepository.findAllByClientId(c.getId());
    }

    public void sendNotificationForRejectingDriveNotEnoughTokens(Drive d) {
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
    }

    public void sendNotificationForRejectingDriveNoAvailableDriver(Drive d) {
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
    }

    public void sendNotificationForAcceptingDrive(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        String route = getRouteString(d);
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.REJECT_DRIVE);
            n.setMessage("Voznja na ruti "+route+" je prihvacena. Automobil stize za ...");
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
        return d.getRoutes().get(0).getStartPosition().getAddress()+" - "+d.getRoutes().get(d.getRoutes().size()-1).getEndPosition().getAddress();
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

    public void sendNotificationForDriverRejectingDrive(Drive d) {
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
    }
}

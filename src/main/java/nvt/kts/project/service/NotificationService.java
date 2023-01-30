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
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    public void sendNotificationsForApprovingPayment(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        Double tokenPrice = systemInfoService.getTokenPrice();
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.APPROVE_PAYMENT);
            n.setMessage("Molim vas odobrite placanje voznje na ruti Trebinje-Beograd i iznosi "+clientDrive.getPrice()+"$" +
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

    public void sendNotificationForRejectingDriveNotEnoghTokens(Drive d) {
        List<Notification> newNotifications = new ArrayList<>();
        List<ClientDrive> clientDrives = clientDriveRepository.getClientDriveByDrive(d.getId());
        for (ClientDrive clientDrive: clientDrives){
            Notification n = new Notification();
            n.setClient(clientDrive.getClient());
            n.setDrive(d);
            n.setReason(NotificationReason.REJECT_DRIVE);
            n.setMessage("Voznja na ruti Trebinje-Beograd je otkazana zbog nedostatka tokena.");
            n.setDateTime(LocalDateTime.now());
            newNotifications.add(n);
            NotificationDTO dto = new NotificationDTO(n);
            dto.setReceiverEmail(n.getClient().getEmail());
            dto.setApprovedPayment(clientDrive.isApproved());
            this.simpMessagingTemplate.convertAndSend("/notification/rejectedPayment",dto);
        }
        notificationRepository.saveAll(newNotifications);
    }
}

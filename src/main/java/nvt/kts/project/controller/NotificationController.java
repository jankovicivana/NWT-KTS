package nvt.kts.project.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.NotificationDTO;
import nvt.kts.project.model.*;
import nvt.kts.project.service.ClientService;
import nvt.kts.project.service.DriveService;
import nvt.kts.project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private DriveService driveService;

    private final SimpMessagingTemplate simpMessagingTemplate;


    @GetMapping("/getNotifications")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<List<NotificationDTO>> getNotifications(Principal principal) {
        List<NotificationDTO> dtos = new ArrayList<>();
        Client c = clientService.getClientByEmail(principal.getName());
        List<Notification> notif = notificationService.getNotifications(c);
        for (Notification n: notif){
            dtos.add(new NotificationDTO(n));
        }
    return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


    @GetMapping("/sendNotif")
    public ResponseEntity<Boolean> sendNotif(Principal principal) {
        NotificationDTO n = new NotificationDTO();
        n.setDriveId(1L);
        n.setDateTime("2022-12-12");
        n.setMessage("mess");
        n.setReason("reason");
        this.simpMessagingTemplate.convertAndSend("/notification/approvePayment",n);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}

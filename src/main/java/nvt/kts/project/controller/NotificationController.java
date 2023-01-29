package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.NotificationDTO;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.Note;
import nvt.kts.project.model.Notification;
import nvt.kts.project.service.ClientService;
import nvt.kts.project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
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
}

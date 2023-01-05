package nvt.kts.project.controller;

import nvt.kts.project.dto.ClientDTO;
import nvt.kts.project.dto.MessageClientDTO;
import nvt.kts.project.dto.MessageDTO;
import nvt.kts.project.dto.OutputMessageDTO;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.Message;
import nvt.kts.project.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/chat")
public class LiveChatController {

    private final SimpMessagingTemplate simpleMessageTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    public LiveChatController(SimpMessagingTemplate simpleMessageTemplate) {
        this.simpleMessageTemplate = simpleMessageTemplate;
    }

    @MessageMapping("send/message")
    public void onReceivedMessage(MessageDTO message){
        this.simpleMessageTemplate.convertAndSend("/chat",message);
    }

    @GetMapping("/getMessages")
    @PreAuthorize("hasAnyRole('client','admin')")
    public ResponseEntity<List<OutputMessageDTO>> getUserMessages(Principal principal) {
        List<OutputMessageDTO> messageDTOS = messageService.getOutputMessages(principal.getName());
        return new ResponseEntity<>(messageDTOS, HttpStatus.OK);
    }



}

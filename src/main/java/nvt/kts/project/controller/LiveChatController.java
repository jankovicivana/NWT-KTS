package nvt.kts.project.controller;

import nvt.kts.project.dto.MessageDTO;
import nvt.kts.project.dto.OutputMessageDTO;
import nvt.kts.project.model.Message;
import nvt.kts.project.service.MessageService;
import nvt.kts.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/chat")
public class LiveChatController {

    private final SimpMessagingTemplate simpleMessageTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

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

    @PostMapping("/saveMessage")
    @PreAuthorize("hasAnyRole('client','admin')")
    public ResponseEntity<String> saveMessage(@RequestBody MessageDTO messageDTO) {
        Message m = new Message();
        m.setSender(userService.findByEmail(messageDTO.getFrom()));
        m.setRecipient(userService.findByEmail(messageDTO.getTo().equals("admins")?"admin@gmail.com":messageDTO.getTo()));
        m.setText(messageDTO.getText());
        messageService.save(m);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}

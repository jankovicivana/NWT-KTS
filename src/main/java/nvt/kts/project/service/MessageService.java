package nvt.kts.project.service;

import nvt.kts.project.dto.MessageClientDTO;
import nvt.kts.project.dto.OutputMessageDTO;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.Message;
import nvt.kts.project.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getUserMessages(String email){
        return messageRepository.findAllBySenderEmailOrRecipientEmail(email,email);
    }

    public void save(Message message){
        messageRepository.save(message);
    }

    public List<OutputMessageDTO> getOutputMessages(String email){
        List<Message> messages = getUserMessages(email);
        List<OutputMessageDTO> messageDTOS = new ArrayList<>();
        for (Message message:messages) {
            MessageClientDTO senderDTO = new MessageClientDTO(message.getSender().getName(),message.getSender().getSurname(),message.getSender().getEmail(),"",message.getSender().getIsSocialLogin());
            MessageClientDTO recipientDTO = new MessageClientDTO(message.getRecipient().getName(),message.getRecipient().getSurname(),message.getRecipient().getEmail(),"",message.getRecipient().getIsSocialLogin());
            if(message.getSender().getRoles().get(0).getName().equals("ROLE_client")){
                Client sender = (Client) message.getSender();
                senderDTO.setPhoto(sender.getPhoto());
            }
            if(message.getRecipient().getRoles().get(0).getName().equals("ROLE_client")){
                Client recipient = (Client) message.getRecipient();
                recipientDTO.setPhoto(recipient.getPhoto());
            }
            OutputMessageDTO out = new OutputMessageDTO(senderDTO,message.getText(),recipientDTO);
            messageDTOS.add(out);
        }
        return messageDTOS;
    }
}

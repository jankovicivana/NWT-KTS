package nvt.kts.project.service;

import nvt.kts.project.model.Message;
import nvt.kts.project.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getUserMessages(String email){
        return messageRepository.findAllBySenderEmailOrRecipientEmail(email,email);
    }
}

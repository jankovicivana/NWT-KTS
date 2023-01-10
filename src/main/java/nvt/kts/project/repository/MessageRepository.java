package nvt.kts.project.repository;

import nvt.kts.project.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllBySenderEmailOrRecipientEmail(String sender_email, String recipient_email);
}

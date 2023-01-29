package nvt.kts.project.repository;

import nvt.kts.project.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findAllByClientId(Long id);
}

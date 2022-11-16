package nvt.kts.project.repository;

import nvt.kts.project.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Driver findByEmail(String email);
}

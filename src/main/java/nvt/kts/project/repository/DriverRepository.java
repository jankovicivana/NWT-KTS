package nvt.kts.project.repository;

import nvt.kts.project.model.Client;
import nvt.kts.project.model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Driver findByEmail(String email);

    Page<Driver> findAllByDeletedIsFalse(Pageable pageable);


    Driver findDriverById(Long id);
}

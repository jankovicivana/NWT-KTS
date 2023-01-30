package nvt.kts.project.repository;

import nvt.kts.project.model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Driver findByEmail(String email);

    Page<Driver> findAllByDeletedIsFalse(Pageable pageable);

    Driver findDriverById(Long id);

    @Query("SELECT d from Driver d where d.active = true and d.available = true and d.car.babiesAllowed = :babiesAllowed and d.car.petFriendly = :petFriendly and d.car.type.type = :type ")
    List<Driver> findActiveAndAvailableDriversByCarCriteria(@Param("type") String type, @Param("babiesAllowed") boolean babiesAllowed, @Param("petFriendly") boolean petFriendly);

    @Query("SELECT d from Driver d where d.active = true and d.car.babiesAllowed = :babiesAllowed and d.car.petFriendly = :petFriendly and d.car.type.type = :type ")
    List<Driver> findActiveDriversByCarCriteria(@Param("type") String type, @Param("babiesAllowed") boolean babiesAllowed, @Param("petFriendly") boolean petFriendly);
}

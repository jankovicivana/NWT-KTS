package nvt.kts.project.repository;

import nvt.kts.project.model.Driver;
import nvt.kts.project.model.DriverActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DriverActivityRepository extends JpaRepository<DriverActivity,Long> {

    @Query("SELECT d from DriverActivity d where d.endTime is null and d.driver.id = :id")
    DriverActivity findUnfinishedLog(@Param("id") Long id);

    @Query("SELECT d from DriverActivity d where d.driver.id = :id ")
    List<DriverActivity> getDriverActivites(@Param("id") Long id);
}

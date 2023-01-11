package nvt.kts.project.repository;

import nvt.kts.project.model.Drive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DriveRepository extends JpaRepository<Drive, Long> {

    // treba gledati da li je rejected ? ? ?
    @Query("SELECT d from Drive d where d.startTime <= :now and d.endTime > :now")
    List<Drive> getCurrentDrives(@Param("now") LocalDateTime now);

}

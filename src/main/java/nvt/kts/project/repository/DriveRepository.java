package nvt.kts.project.repository;

import nvt.kts.project.model.ClientDrive;
import nvt.kts.project.model.Drive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DriveRepository extends JpaRepository<Drive, Long> {

    // treba gledati da li je rejected ? ? ?
    @Query("SELECT d from Drive d where d.startTime <= :now and d.endTime > :now")
    List<Drive> getCurrentDrives(@Param("now") LocalDateTime now);

    @Query("SELECT d from Drive d where d.endTime < :now and :email IN (SELECT p.client.email from d.passengers p)")
    List<Drive> getClientDriveHistory(@Param("now") LocalDateTime now,@Param("email") String email);

    @Query("SELECT d from Drive d where d.endTime < :now and d.driver.email = :email")
    List<Drive> getDriverDriveHistory(@Param("now") LocalDateTime now,@Param("email") String email);

    @Query("SELECT d from Drive d where d.endTime < :now")
    List<Drive> getAllDrives(@Param("now") LocalDateTime now);

    @Query("SELECT d from Drive d where d.startTime >= :start and d.endTime <= :end and :email IN (SELECT p.client.email from d.passengers p)")
    List<Drive> getDrivesByClientDate(@Param("start") LocalDateTime start,@Param("end") LocalDateTime end,@Param("email") String email);

    @Query("SELECT d from Drive d where d.startTime >= :start and d.endTime <= :end and d.driver.email = :email")
    List<Drive> getDrivesByDriverDate(@Param("start") LocalDateTime start,@Param("end") LocalDateTime end,@Param("email") String email);

    @Query("SELECT d from Drive d where d.startTime >= :start and d.endTime <= :end")
    List<Drive> getDrivesByDate(@Param("start") LocalDateTime start,@Param("end") LocalDateTime end);


}

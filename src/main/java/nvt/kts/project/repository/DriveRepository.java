package nvt.kts.project.repository;

import nvt.kts.project.model.Drive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface DriveRepository extends JpaRepository<Drive, Long> {

    // SCHEDULED ili EMPTY
    @Query("SELECT d from Drive d where d.status = 2 or d.status = 8")
    List<Drive> getCurrentDrives(@Param("now") LocalDateTime now);

    @Query("SELECT d from Drive d where d.driver.email = :driver and (d.status = 2 or (d.status = 1 and d.reservation is null and d.startTime is null )) ")
    List<Drive> getCurrentDriverDrive(@Param("driver") String driver);

    @Query("SELECT d from Drive d where d.status = 2 and :driver IN (SELECT p.client.email from d.passengers p)")
    List<Drive> getCurrentClientDrive(@Param("driver") String driver);

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

    @Query("SELECT d from Drive d where d.driver.id = :id  and d.reservation is not null and d.reservation.start < :bound and d.reservation.start > :now")
    List<Drive> getReservations(@Param("id")Long id,@Param("bound") LocalDateTime bound,@Param("now") LocalDateTime now);

    @Query("SELECT d from Drive d where (d.status = 6 or d.status = 1 or d.status = 2) and d.driver.email = :email ")
    List<Drive> getFutureDriverDrives(@Param("email") String email);

    @Query("SELECT d from Drive d where d.status = 8 and d.driver.email = :email")
    List<Drive> getDriverEmptyDrives(@Param("email") String email);

    @Query("SELECT d from Drive d where d.status = 8")
    List<Drive> getEmptyDrives();

    @Query("SELECT d from Drive d where d.status = 6 and d.driver.email = :email")
    List<Drive> getGoingToClientDrives(@Param("email") String email);

    @Query("SELECT d from Drive d where d.reservation is not null and d.reservation.id = :id")
    Drive getDriveByReservation(@Param("id") Long id);
}

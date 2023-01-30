package nvt.kts.project.repository;

import nvt.kts.project.model.ClientDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientDriveRepository extends JpaRepository<ClientDrive,Long> {

    @Query("SELECT d from ClientDrive d where d.drive.id = :id")
    List<ClientDrive> getClientDriveByDrive(@Param("id") Long id);

    @Query("SELECT d from ClientDrive d where d.drive.id = :driveId and d.client.id = :id")
    ClientDrive getClientDriveByDriveAndClient(@Param("id")Long id,@Param("driveId") Long driveId);
}
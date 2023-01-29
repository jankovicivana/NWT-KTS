package nvt.kts.project.repository;

import nvt.kts.project.model.ClientDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientDriveRepository extends JpaRepository<ClientDrive,Long> {

    @Query("SELECT d from ClientDrive d where d.drive = :id")
    List<ClientDrive> getClientDriveByDrive(@Param("id") Long id);

    @Query("SELECT d from ClientDrive d where d.drive = :driveId and d.client = :id")
    ClientDrive getClientDriveByDriveAndClient(Long id, Long driveId);
}

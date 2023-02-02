package nvt.kts.project.repository;

import nvt.kts.project.model.ClientDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ClientDriveRepository extends JpaRepository<ClientDrive,Long> {

    @Query("SELECT d from ClientDrive d where d.drive.id = :id")
    List<ClientDrive> getClientDriveByDrive(@Param("id") Long id);

    @Query("SELECT d from ClientDrive d where d.drive.id = :driveId and d.client.id = :id")
    ClientDrive getClientDriveByDriveAndClient(@Param("id")Long id,@Param("driveId") Long driveId);

    @Query("SELECT d from ClientDrive d where d.favourite = true and d.client.email = :email")
    List<ClientDrive> getFavouriteDrives(@Param("email") String email);
}

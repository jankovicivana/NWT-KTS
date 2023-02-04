package nvt.kts.project.repository;

import nvt.kts.project.model.Drive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.testng.Assert.assertEquals;

@SpringBootTest
public class DriveRepositoryTest {

    @Autowired
    private DriveRepository driveRepository;


    @Test
    public void shouldGetOneEmptyDriveByDriver(){
        List<Drive> cd = driveRepository.getDriverEmptyDrives("driver@gmail.com");
        System.out.print(cd);
        Assertions.assertEquals(cd.size(),1);
    }

    @Test
    public void shouldGetNoEmptyDriveByDriver(){
        List<Drive> cd = driveRepository.getDriverEmptyDrives("wrongEmail@gmail.com");
        Assertions.assertEquals(cd.size(),0);
    }

    @Test
    public void shouldGetThreeEmptyDriveByDriver(){
        List<Drive> cd = driveRepository.getDriverEmptyDrives("driver11@gmail.com");
        Assertions.assertEquals(cd.size(),3);
    }

    @Test
    public void shouldGetReservationsOfDriver(){
        long l = 7;
        List<Drive> cd = driveRepository.getReservations(l, LocalDateTime.now(),LocalDateTime.now().minusYears(1000));
        Assertions.assertEquals(cd.size(), 0);
    }

    @Test
    public void shouldGetCurrentDriverDrives(){
        List<Drive> drives = driveRepository.getCurrentDriverDrive("test@gmail.com");
        assertEquals(drives.size(), 1);
    }

    @Test
    public void shouldNotGetCurrentDriverDrives(){
        List<Drive> drives = driveRepository.getCurrentDriverDrive("driver11@gmail.com");
        assertEquals(drives.size(), 0);
    }
}

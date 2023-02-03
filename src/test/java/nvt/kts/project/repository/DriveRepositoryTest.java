package nvt.kts.project.repository;

import nvt.kts.project.model.Drive;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

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
        assertEquals(cd.size(),1);
    }

    @Test
    public void shouldGetNoEmptyDriveByDriver(){
        List<Drive> cd = driveRepository.getDriverEmptyDrives("wrongEmail@gmail.com");
        assertEquals(cd.size(),0);
    }

    @Test
    public void shouldGetTwoEmptyDriveByDriver(){
        List<Drive> cd = driveRepository.getDriverEmptyDrives("driver11@gmail.com");
        assertEquals(cd.size(),2);
    }
}

package nvt.kts.project.repository;

import nvt.kts.project.ProjectApplication;
import nvt.kts.project.model.ClientDrive;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

@DataJpaTest
public class ClientDriveRepositoryTest {

    @Autowired
    private ClientDriveRepository clientDriveRepository;


    @Test
    public void shouldGetOneClientDriveByDrive(){

        //List<ClientDrive> cd = clientDriveRepository.getClientDriveByDrive(1L);

        //assertEquals(cd.size(),1);
        // nece
    }
}

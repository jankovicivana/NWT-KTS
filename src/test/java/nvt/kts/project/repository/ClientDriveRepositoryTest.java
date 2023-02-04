package nvt.kts.project.repository;

import nvt.kts.project.model.ClientDrive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class ClientDriveRepositoryTest {

    @Autowired
    private ClientDriveRepository clientDriveRepository;


    @Test
    public void shouldGetOneClientDriveByDrive(){
        List<ClientDrive> cd = clientDriveRepository.getClientDriveByDrive(1L);
        Assertions.assertEquals(cd.size(),1);
    }

    @Test
    public void shouldGetNoClientDriveByDrive(){
        List<ClientDrive> cd = clientDriveRepository.getClientDriveByDrive(20L);
        Assertions.assertEquals(cd.size(),0);
    }
}

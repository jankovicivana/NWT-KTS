package nvt.kts.project.repository;

import nvt.kts.project.model.ClientDrive;
import nvt.kts.project.model.SystemInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class SystemInfoRepositoryTest {

    @Autowired
    private SystemInfoRepository systemInfoRepository;


    @Test
    public void testGetSystemInfo(){
        SystemInfo cd = systemInfoRepository.getSystemInfo();
        Assertions.assertEquals(cd.getTokenPrice(),5.0);
    }

}

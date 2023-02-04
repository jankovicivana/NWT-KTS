package nvt.kts.project.repository;

import nvt.kts.project.model.DriverActivity;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DriverActivityRepositoryTest {

    @Autowired
    private DriverActivityRepository driverActivityRepository;

    @Test
    public void shouldReturnDriverActivities(){
        List<DriverActivity> activityList = this.driverActivityRepository.getDriverActivites(7L);
        Assertions.assertEquals(2, activityList.size());
    }

    @Test
    public void shouldReturnNoDriverActivities(){
        List<DriverActivity> activityList = this.driverActivityRepository.getDriverActivites(8L);
        Assertions.assertEquals(0, activityList.size());
    }
}

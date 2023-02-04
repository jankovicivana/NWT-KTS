package nvt.kts.project.repository;

import nvt.kts.project.model.Driver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DriverRepositoryTest {

    @Autowired
    private DriverRepository driverRepository;

    @Test
    public void testActiveAndAvailableDrivers(){
        List<Driver> drivers = driverRepository.findActiveAndAvailableDriversByCarCriteria("Van XL",true,true);
        Assertions.assertEquals(drivers.size(),2);
    }

    @Test
    public void testActiveAndAvailableDriversDifferentParam(){
        List<Driver> drivers = driverRepository.findActiveAndAvailableDriversByCarCriteria("Van XL",false,false);
        Assertions.assertEquals(drivers.size(),2);
    }

    @Test
    public void testActiveDrivers(){
        List<Driver> drivers = driverRepository.findActiveDriversByCarCriteria("Van XL",true,true);
        Assertions.assertEquals(drivers.size(),0);
    }

}

package nvt.kts.project.controller.driveController;

import nvt.kts.project.model.Driver;
import nvt.kts.project.service.DriverService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.testng.Assert.assertEquals;

@SpringBootTest(properties="spring.datasource.url=jdbc:h2:mem:testdb", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DriveApprovingNoAvailableDrivers {


    @Autowired
    private DriverService driverService;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void checkIfAllApprovedNoAvailableDrivers(){
        List<Driver> drivers = driverService.getAll();
        for(Driver d: drivers){
            driverService.addDriverActivityForUnactiveDriver(d);
        }
        ResponseEntity<String> response = restTemplate.getForEntity("/api/drive/checkIfAllApproved/"+5, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}

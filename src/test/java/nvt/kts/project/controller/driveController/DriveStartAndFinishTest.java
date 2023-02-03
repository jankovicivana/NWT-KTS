package nvt.kts.project.controller.driveController;


import nvt.kts.project.dto.EmptyDriveDTO;
import nvt.kts.project.dto.JwtAuthenticationRequest;
import nvt.kts.project.dto.ScheduleInfoDTO;
import nvt.kts.project.dto.UserTokenState;
import nvt.kts.project.model.Drive;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.testng.Assert.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(properties="spring.datasource.url=jdbc:h2:mem:testdb", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DriveStartAndFinishTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headersTestDriver;

    @BeforeEach
    public void setUp() {
        JwtAuthenticationRequest requestTestDriver = new JwtAuthenticationRequest();
        requestTestDriver.setUsername("test@gmail.com");
        requestTestDriver.setPassword("pass");
        ResponseEntity<UserTokenState> resTest = restTemplate
                .postForEntity("/auth/login/", requestTestDriver, UserTokenState.class);
        String tokenTestDriver = "Bearer " + resTest.getBody().getAccessToken();
        headersTestDriver = new HttpHeaders();
        headersTestDriver.add("Authorization", tokenTestDriver);
        headersTestDriver.setContentType(MediaType.APPLICATION_JSON);
        headersTestDriver.add("Content-Type", "application/json");
    }

    @Test
    @Order(1)
    void shouldGoToClient(){
        EmptyDriveDTO dto = new EmptyDriveDTO();
        Drive drive = new Drive();
        drive.setId(8L);
        dto.setDrive(drive);
        dto.setDuration(5.0);
        HttpEntity<EmptyDriveDTO> entity = new HttpEntity<>(dto, headersTestDriver);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/drive/goToClient", entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Driver going to client.");
    }

    @Test
    @Order(2)
    void shouldFinishDrive(){
        HttpEntity<Long> entity = new HttpEntity<>(8L, headersTestDriver);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/drive/finish", entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Drive finished.");
    }

}

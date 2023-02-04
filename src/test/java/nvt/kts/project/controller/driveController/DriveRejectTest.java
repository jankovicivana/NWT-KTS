package nvt.kts.project.controller.driveController;

import nvt.kts.project.dto.DriveDTO;
import nvt.kts.project.dto.JwtAuthenticationRequest;
import nvt.kts.project.dto.UserTokenState;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.testng.Assert.assertEquals;


@SpringBootTest(properties="spring.datasource.url=jdbc:h2:mem:testdb", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DriveRejectTest {

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
    void shouldRejectDrive(){
        DriveDTO dto = new DriveDTO();
        dto.setId(8L);
        dto.setRejectionReason("neki razlog");
        HttpEntity<DriveDTO> entity = new HttpEntity<>(dto, headersTestDriver);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/drive/saveRejectionDriveReason", entity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

}
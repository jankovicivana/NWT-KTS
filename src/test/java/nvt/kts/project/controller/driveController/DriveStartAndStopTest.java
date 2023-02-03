package nvt.kts.project.controller.driveController;

import nvt.kts.project.dto.*;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.Drive;
import nvt.kts.project.service.ClientService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties"})
public class DriveStartAndStopTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private ScheduleInfoDTO info;

    private HttpHeaders headersClient;

    private HttpHeaders headersDriver;

    private HttpHeaders headersTestDriver;

    @BeforeEach
    public void setUp() {

        JwtAuthenticationRequest request = new JwtAuthenticationRequest();
        request.setUsername("ivanaj0610@gmail.com");
        request.setPassword("pass");
        ResponseEntity<UserTokenState> response = restTemplate
                .postForEntity("/auth/login/", request, UserTokenState.class);
        String token = "Bearer " + response.getBody().getAccessToken();
        headersClient = new HttpHeaders();
        headersClient.add("Authorization", token);
        headersClient.setContentType(MediaType.APPLICATION_JSON);
        headersClient.add("Content-Type", "application/json");

        JwtAuthenticationRequest requestDriver = new JwtAuthenticationRequest();
        requestDriver.setUsername("driver@gmail.com");
        requestDriver.setPassword("pass");
        ResponseEntity<UserTokenState> res = restTemplate
                .postForEntity("/auth/login/", requestDriver, UserTokenState.class);
        String tokenDriver = "Bearer " + res.getBody().getAccessToken();
        headersDriver = new HttpHeaders();
        headersDriver.add("Authorization", tokenDriver);
        headersDriver.setContentType(MediaType.APPLICATION_JSON);
        headersDriver.add("Content-Type", "application/json");

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
    @Order(2)
    public void shouldSaveDrive(){
        this.info = new ScheduleInfoDTO();
        info.setCar("Van XL");
        info.setBabies(false);
        info.setPet(false);
        info.setPrice(20.0);
        info.setSplitFaire(false);
        info.setReservation(false);
        info.setDistance(3.0);
        info.setDuration(3.0);
        info.setPassengers(new ArrayList<>());
        RouteDTO r = new RouteDTO();
        PositionDTO start = new PositionDTO();
        start.setLon(40.0);
        start.setLat(19.0);
        start.setAddress("Strazilovska 14");

        PositionDTO end = new PositionDTO();
        end.setLon(30.0);
        end.setLat(19.0);
        end.setAddress("Puskinova 16");

        r.setType("recommended");
        r.setStart(start);
        r.setEnd(end);

        List<RouteDTO> routes = new ArrayList<>();
        routes.add(r);
        info.setRoutes(routes);

        HttpEntity<ScheduleInfoDTO> entity = new HttpEntity<>(info, headersClient);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/drive/saveDrive", entity, String.class);

        String message = response.getBody();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(message, "Successfully saved drive.");
    }

    @Test
    @Order(3)
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
    @Order(4)
    void shouldStartDrive(){
        HttpEntity<Long> entity = new HttpEntity<>(8L, headersTestDriver);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/drive/start", entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Drive started.");
    }

    @Test
    @Order(5)
    void shouldStopDrive(){
        HttpEntity<Long> entity = new HttpEntity<>(8L, headersTestDriver);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/drive/stop", entity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), "Drive stopped.");
    }

}

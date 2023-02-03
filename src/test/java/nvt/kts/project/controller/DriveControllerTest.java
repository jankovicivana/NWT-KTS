package nvt.kts.project.controller;

import nvt.kts.project.dto.*;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.Drive;
import nvt.kts.project.model.Driver;
import nvt.kts.project.service.ClientService;
import nvt.kts.project.service.DriveService;
import nvt.kts.project.service.DriverService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

@SpringBootTest(properties="spring.datasource.url=jdbc:h2:mem:testdb", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DriveControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClientService clientService;

    @Autowired
    private DriverService driverService;

    private ScheduleInfoDTO info;

    private HttpHeaders headersClient;

    private HttpHeaders headersDriver;

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
    }


    @Test
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
    void shouldGoToClient(){
        EmptyDriveDTO dto = new EmptyDriveDTO();
        Drive drive = new Drive();
        drive.setId(1L);
        dto.setDrive(drive);
        dto.setDuration(5.0);
        HttpEntity<EmptyDriveDTO> entity = new HttpEntity<>(dto, headersDriver);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/drive/goToClient", entity, String.class);

    }

    @Test
    void shouldStartDrive(){

    }

    @Test
    void shouldStopDrive(){

    }

    @Test
    void shouldFinishDrive(){

    }

    @Test
    void shouldRejectDrive(){
        DriveDTO dto = new DriveDTO();
        dto.setId(1L);
        dto.setRejectionReason("neki razlog");
        HttpEntity<DriveDTO> entity = new HttpEntity<>(dto, headersDriver);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/drive/saveRejectionDriveReason", entity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void successfulCheckIfAllApproved(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/drive/checkIfAllApproved/"+5, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
    }

    @Test
    void checkIfAllApprovedNoEnoughTokens(){
        Client c = clientService.getClientByEmail("client2@gmail.com");
        c.setTokens(1);
        clientService.save(c);
        ResponseEntity<String> response = restTemplate.getForEntity("/api/drive/checkIfAllApproved/"+5, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

//    @Test
//    void checkIfAllApprovedNoAvailableDrivers(){
//        List<Driver> drivers = driverService.
//        ResponseEntity<String> response = restTemplate.getForEntity("/api/drive/checkIfAllApproved/"+5, String.class);
//        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
//    }


}

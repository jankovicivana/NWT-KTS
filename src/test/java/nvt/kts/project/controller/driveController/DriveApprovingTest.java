package nvt.kts.project.controller.driveController;


import nvt.kts.project.dto.JwtAuthenticationRequest;
import nvt.kts.project.dto.ScheduleInfoDTO;
import nvt.kts.project.dto.UserTokenState;
import nvt.kts.project.model.Client;
import nvt.kts.project.service.ClientService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.testng.Assert.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(properties="spring.datasource.url=jdbc:h2:mem:testdb", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DriveApprovingTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClientService clientService;

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
    @Order(1)
    void checkIfAllApprovedNoEnoughTokens(){
        Client c = clientService.getClientByEmail("client2@gmail.com");
        c.setTokens(1);
        clientService.save(c);
        ResponseEntity<String> response = restTemplate.getForEntity("/api/drive/checkIfAllApproved/"+5, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(2)
    void successfulCheckIfAllApproved(){
        Client c = clientService.getClientByEmail("client2@gmail.com");
        c.setTokens(10);
        clientService.save(c);
        ResponseEntity<String> response = restTemplate.getForEntity("/api/drive/checkIfAllApproved/"+5, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
    }
}

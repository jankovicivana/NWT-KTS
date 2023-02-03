package nvt.kts.project.controller;

import nvt.kts.project.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DriveControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private ScheduleInfoDTO info;

    private HttpHeaders headers;

    @BeforeEach
    public void setUp() {

        JwtAuthenticationRequest request = new JwtAuthenticationRequest();
        request.setUsername("ivanaj0610@gmail.com");
        request.setPassword("pass");

        ResponseEntity<UserTokenState> response = restTemplate
                .postForEntity("/auth/login/", request, UserTokenState.class);

        String token = "Bearer " + response.getBody().getAccessToken();
        headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Content-Type", "application/json");
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

        HttpEntity<ScheduleInfoDTO> entity = new HttpEntity<>(info, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/drive/saveDrive", entity, String.class);

        String message = response.getBody();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(message, "Successfully saved drive.");
    }

    @Test
    void shouldGoToClient(){

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

    }

}

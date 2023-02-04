package nvt.kts.project.repository;

import nvt.kts.project.model.Route;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RouteRepositoryTest {

    @Autowired
    private RouteRepository routeRepository;

    @Test
    public void shouldGetRoutes(){
        List<Route> routeList = routeRepository.finAllByDrive(7L);
        Assertions.assertEquals(1, routeList.size());
    }

    @Test
    public void shouldGetNoRoutes(){
        List<Route> routeList = routeRepository.finAllByDrive(3L);
        Assertions.assertEquals(0, routeList.size());
    }
}

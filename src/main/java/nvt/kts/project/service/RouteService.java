package nvt.kts.project.service;

import nvt.kts.project.model.Route;
import nvt.kts.project.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {


    @Autowired
    private RouteRepository routeRepository;

    public Route save(Route r){
        return this.routeRepository.save(r);
    }

    public List<Route> getRoutes(Long id) {
        return this.routeRepository.finAllByDrive(id);
    }
}

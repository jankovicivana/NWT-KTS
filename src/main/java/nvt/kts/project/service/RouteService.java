package nvt.kts.project.service;

import nvt.kts.project.model.Route;
import nvt.kts.project.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService {


    @Autowired
    private RouteRepository routeRepository;

    public Route save(Route r){
        return this.routeRepository.save(r);
    }
}

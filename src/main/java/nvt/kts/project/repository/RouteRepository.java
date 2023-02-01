package nvt.kts.project.repository;

import nvt.kts.project.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT r from Route r where r.drive.id = :id")
    List<Route> finAllByDrive(@Param("id") Long id);
}

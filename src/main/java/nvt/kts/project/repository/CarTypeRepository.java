package nvt.kts.project.repository;

import nvt.kts.project.model.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarTypeRepository extends JpaRepository<CarType,Long> {
}

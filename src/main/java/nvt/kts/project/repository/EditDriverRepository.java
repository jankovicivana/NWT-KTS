package nvt.kts.project.repository;

import nvt.kts.project.model.EditDriver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EditDriverRepository extends JpaRepository<EditDriver,Long> {

    @Query("SELECT d from EditDriver d where d.status = 1")
    Page<EditDriver> findAllByStatus(Pageable pageable);
}

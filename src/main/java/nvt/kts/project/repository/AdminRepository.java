package nvt.kts.project.repository;

import nvt.kts.project.model.Admin;
import nvt.kts.project.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByEmail(String email);

    Admin findAdminById(Long id);
}

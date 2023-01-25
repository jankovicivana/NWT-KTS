package nvt.kts.project.repository;

import nvt.kts.project.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmail(String email);

    Page<Client> findAllByDeletedIsFalse(Pageable pageable);

    Client findClientById(Long id);

}

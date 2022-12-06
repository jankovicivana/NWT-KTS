package nvt.kts.project.repository;

import nvt.kts.project.model.SystemInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SystemInfoRepository extends JpaRepository<SystemInfo, Long> {
    @Query("SELECT b from SystemInfo b where b.id=1")
    SystemInfo getSystemInfo();
}

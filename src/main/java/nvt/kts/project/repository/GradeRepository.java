package nvt.kts.project.repository;

import nvt.kts.project.model.Grade;
import nvt.kts.project.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("SELECT b from Grade b where b.clientId=:clientId and b.driveId=:drive")
    Grade getGrade(Long clientId,Long drive);

    List<Grade> getAllByDriveId(Long driveId);

}

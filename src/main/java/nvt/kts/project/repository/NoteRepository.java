package nvt.kts.project.repository;

import nvt.kts.project.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT b from Note b where b.clientId=:id")
    List<Note> findAllByClientId(Long id);


    @Query("SELECT MAX(id) FROM Note")
    Long getNoteWithMaxId();

    @Query("SELECT b from Note b where b.driverId=:id")
    List<Note> findAllByDriverId(Long id);
}

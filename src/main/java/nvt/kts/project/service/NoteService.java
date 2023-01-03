package nvt.kts.project.service;

import nvt.kts.project.model.Client;
import nvt.kts.project.model.Note;
import nvt.kts.project.model.User;
import nvt.kts.project.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserService userService;

//    public List<Note> findByClientEmail(String email) {
//        User c = userService.findByEmail(email);
//        return noteRepository.findAllByClientId(c.getId());
//    }

    public void saveNotes(List<Note> notes){
        noteRepository.saveAll(notes);
    }


    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public Long getMaxId() {
        Long id = noteRepository.getNoteWithMaxId();
        return id + 1;
    }

    public List<Note> getNotesByUser(Long id) {
        List<Note> notes = noteRepository.findAllByClientId(id);
        if (notes.size()==0){
            return noteRepository.findAllByDriverId(id);
        }else {return notes;}
    }
}

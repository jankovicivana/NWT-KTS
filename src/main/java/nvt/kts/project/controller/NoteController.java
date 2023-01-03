package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.DriverDTO;
import nvt.kts.project.dto.NoteDTO;
import nvt.kts.project.model.Driver;
import nvt.kts.project.model.Note;
import nvt.kts.project.service.AdminService;
import nvt.kts.project.service.ClientService;
import nvt.kts.project.service.DriverService;
import nvt.kts.project.service.NoteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private ModelMapper mapper;


    @DeleteMapping("/deleteNote/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getNotes/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<Note>> getNotes(@PathVariable Long id) {
        List<Note> notes = noteService.getNotesByUser(id);
        return new ResponseEntity<>(notes,HttpStatus.OK);
    }

    @GetMapping("/getMaxId")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Long> getMaxNoteId() {
        Long id = noteService.getMaxId();
        return new ResponseEntity<>(id,HttpStatus.OK);
    }

    @PostMapping("/saveNotes")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> saveNotes(@RequestBody List<NoteDTO> noteDTOS) {
        List<Note> notes = new ArrayList<>();
        for (NoteDTO dto: noteDTOS){
            Note t = mapper.map(dto,Note.class);
            t.setAdminId(dto.getAdminId());
            t.setClientId(dto.getClientId());
            t.setDriverId(dto.getDriverId());
            notes.add(t);
        }
        noteService.saveNotes(notes);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

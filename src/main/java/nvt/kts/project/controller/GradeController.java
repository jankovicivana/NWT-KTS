package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.GradeDTO;
import nvt.kts.project.dto.NoteDTO;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.Driver;
import nvt.kts.project.model.Grade;
import nvt.kts.project.model.Note;
import nvt.kts.project.service.ClientService;
import nvt.kts.project.service.DriverService;
import nvt.kts.project.service.GradeService;
import nvt.kts.project.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/grade")
public class GradeController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/getGrade/{id}")
    @PreAuthorize("hasAnyRole('admin','client')")
    public ResponseEntity<GradeDTO> getGrade(@PathVariable Long id, Principal principal) {
        Client c = clientService.getClientByEmail("ivanaj0610@gmail.com");
        //Drive d = driveService.getDriveById(id);
        //provjera da li je putnik u voznji
        Grade grade = gradeService.getGrade(id,c.getId());
        GradeDTO dto = new GradeDTO();
        Driver d;
        if (grade == null){
            //preuzmi iz voznje id vozaca
            d = driverService.findDriverById(Long.parseLong("7"));
        }else {
            dto = modelMapper.map(grade,GradeDTO.class);
            d = driverService.findDriverById(grade.getDriverId());
        }
        //setuj vrstu auta
        dto.setDriveId(id);
        dto.setDriverId(d.getId());
        dto.setDriverName(d.getName());
        dto.setDriverSurname(d.getSurname());
        return new ResponseEntity<>(dto,HttpStatus.OK);    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> saveNotes(@RequestBody GradeDTO gradeDTO,Principal principal) {
        Grade g = modelMapper.map(gradeDTO,Grade.class);
        g.setClientId(clientService.getClientByEmail("ivanaj0610@gmail.com").getId());
        gradeService.save(g);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

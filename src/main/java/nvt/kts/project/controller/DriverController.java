package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.CarDTO;
import nvt.kts.project.dto.DriverCarDTO;
import nvt.kts.project.dto.DriverDTO;
import nvt.kts.project.model.Driver;
import nvt.kts.project.model.EditDriver;
import nvt.kts.project.model.RequestStatus;
import nvt.kts.project.service.CarService;
import nvt.kts.project.service.DriverService;
import nvt.kts.project.service.EditDriverService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EditDriverService editDriverService;

    @Autowired
    private CarService carService;


    @GetMapping("/getDriver/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id , Principal principal) {
        Driver u = driverService.getDriverById(id);
        DriverDTO dto = mapper.map(u, DriverDTO.class);
        dto.setRole("Driver");
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/getDriver")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<DriverDTO> getDriver(Principal principal) {
        Driver u = driverService.getDriverByEmail(principal.getName());
        DriverDTO dto = mapper.map(u, DriverDTO.class);
        dto.setRole("Driver");
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/getDriverChanges/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DriverCarDTO> getDriverChangesById(@PathVariable Long id, Principal principal) {
        EditDriver d = editDriverService.getDriverChangesById(id);
        DriverCarDTO dto = mapper.map(d,DriverCarDTO.class);
        dto.setType(carService.findCarTypeById(dto.getType()).getType());
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @GetMapping(value = "/isActive",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<Boolean> isActive( Principal principal) {
        Boolean isActive = driverService.isActive(driverService.getDriverByEmail(principal.getName()));
        return new ResponseEntity<>(isActive,HttpStatus.OK);
    }

    @GetMapping(value = "/hasWorkingTime/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('driver','client','admin')")
    public ResponseEntity<Boolean> hasWorkingTime(@PathVariable Long id, Principal principal) {
        Driver d = driverService.getDriverById(id);
        Boolean isActive = driverService.hasWorkingHours(d);
        return new ResponseEntity<>(isActive,HttpStatus.OK);
    }

    @PostMapping("/acceptDriverChanges")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Boolean> acceptDriverChanges(@RequestBody DriverCarDTO dto, Principal principal) {
        EditDriver e = mapper.map(dto,EditDriver.class);
        Driver driver = driverService.findDriverById(e.getDriverId());
        Driver updatedDriver = driverService.mapDriverInfo(dto, driver);
        driverService.save(updatedDriver);
        e.setStatus(RequestStatus.ACCEPTED);
        editDriverService.save(e);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    @PostMapping("/rejectDriverChanges")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Boolean> rejectDriverChanges(@RequestBody DriverCarDTO dto, Principal principal) {
        EditDriver e = mapper.map(dto,EditDriver.class);
        e.setStatus(RequestStatus.REJECTED);
        editDriverService.save(e);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    @PostMapping("/changeDriverActivity")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<String> changeDriverActivity(@RequestBody Boolean active, Principal principal) {
        Driver d = driverService.getDriverByEmail(principal.getName());
        if (active){
            driverService.finishActivityLog(d);
        }else {
            driverService.createActivityLog(d);
        }
        d.setActive(!active);
        driverService.save(d);
        return new ResponseEntity<>("Successful",HttpStatus.OK);
    }
}

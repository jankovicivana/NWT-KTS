package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.CarDTO;
import nvt.kts.project.dto.CarTypeDTO;
import nvt.kts.project.model.Car;
import nvt.kts.project.model.CarType;
import nvt.kts.project.model.Driver;
import nvt.kts.project.service.CarService;
import nvt.kts.project.service.DriverService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/getCarTypes")
    @PreAuthorize("hasAnyRole('driver','admin')")
    public ResponseEntity<List<CarTypeDTO>> getCarTypes(Principal principal) {
        List<CarType> types = carService.getCarTypes();
        List<CarTypeDTO> dtos = new ArrayList<>();
        for(CarType type:types){
            dtos.add(mapper.map(type,CarTypeDTO.class));
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/saveCar")
    @PostAuthorize("hasAnyRole('driver','admin')")
    public ResponseEntity<Void> saveCar(@RequestBody CarDTO dto, Principal principal) {
        Car car = mapper.map(dto, Car.class);
        Driver driver = driverService.findDriverById(dto.getDriverId());
        car.setType(carService.findCarTypeByName(dto.getType()));
        car.setDriver(driver);
        Car savedCar = carService.save(car);
        driver.setCar(savedCar);
        driverService.save(driver);
        return new ResponseEntity<>( HttpStatus.OK);
    }
}

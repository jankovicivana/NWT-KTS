package nvt.kts.project.service;

import nvt.kts.project.dto.DriverCarDTO;
import nvt.kts.project.dto.DriverDTO;
import nvt.kts.project.dto.UserRequest;
import nvt.kts.project.model.Driver;
import nvt.kts.project.model.Role;
import nvt.kts.project.repository.DriverRepository;
import nvt.kts.project.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CarService carService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Driver save(Driver driver){ return driverRepository.save(driver);}

    public Driver save(UserRequest userRequest) {
        Driver driver = new Driver();
        driver.setEmail(userRequest.getEmail());
        driver.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        driver.setName(userRequest.getName());
        driver.setSurname(userRequest.getSurname());
        driver.setCity(userRequest.getCity());
        driver.setEnabled(false);
        driver.setPhoneNumber(userRequest.getPhoneNumber());
        List<Role> roles = roleRepository.findByName("ROLE_" + userRequest.getRole().toLowerCase(Locale.ROOT));
        driver.setRoles(roles);
        driver.setPhoto("unknown.jpg");

        return this.driverRepository.save(driver);
    }

    public List<Driver> getDrivers(Pageable pageable, HttpHeaders hh) {
        Page<Driver> drivers =driverRepository.findAllByDeletedIsFalse(pageable);
        hh.add("Total-items", Long.toString(drivers.getTotalElements()));
        return drivers.getContent();
    }

    public Driver mapDriverInfo(DriverCarDTO dto, Driver driver) {
        DriverDTO updatedDriverDTO = new DriverDTO(dto, driver);
        Driver updatedDriver = mapper.map(updatedDriverDTO,Driver.class);
        updatedDriver.setCar(driver.getCar());
        updatedDriver.getCar().setBabiesAllowed(dto.getBabiesAllowed());
        updatedDriver.getCar().setPetFriendly(dto.getPetFriendly());
        updatedDriver.getCar().setType(carService.findCarTypeById(dto.getType()));
        updatedDriver.setRoles(driver.getRoles());
        return updatedDriver;
    }

    public Driver findDriverById(Long id) {
        return driverRepository.findDriverById(id);
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public Driver getDriverByEmail(String email) {
        return driverRepository.findByEmail(email);
    }

    public Driver getDriverById(Long id) {
        return driverRepository.findDriverById(id);
    }
}

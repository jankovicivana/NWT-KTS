package nvt.kts.project.service;

import nvt.kts.project.dto.UserRequest;
import nvt.kts.project.model.Driver;
import nvt.kts.project.model.Role;
import nvt.kts.project.repository.DriverRepository;
import nvt.kts.project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void save(Driver driver){driverRepository.save(driver);}

    public Driver save(UserRequest userRequest) {
        Driver driver = new Driver();
        driver.setEmail(userRequest.getEmail());
        driver.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        driver.setName(userRequest.getName());
        driver.setSurname(userRequest.getSurname());
        driver.setCity(userRequest.getCity());
        driver.setEnabled(true);
        driver.setPhoneNumber(userRequest.getPhoneNumber());
        List<Role> roles = roleRepository.findByName("ROLE_" + userRequest.getRole().toLowerCase(Locale.ROOT));
        driver.setRoles(roles);
        driver.setPhoto("");

        return this.driverRepository.save(driver);
    }
}

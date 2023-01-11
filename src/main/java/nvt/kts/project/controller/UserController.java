package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.ClientDTO;
import nvt.kts.project.dto.DriverDTO;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.Driver;
import nvt.kts.project.model.User;
import nvt.kts.project.service.ClientService;
import nvt.kts.project.service.DriverService;
import nvt.kts.project.service.EmailService;
import nvt.kts.project.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import org.springframework.data.domain.Pageable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EmailService emailService;


    @GetMapping("/getUser/{email}")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if(user == null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/getLoggedUser")
    @PreAuthorize("hasAnyRole('client','admin')")
    public ResponseEntity<User> getLoggedUser(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if(user == null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        System.out.print(user.getId());
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/getClient")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<ClientDTO> getClient(Principal principal) {
        Client u = clientService.getClientByEmail(principal.getName());
        ClientDTO dto = mapper.map(u,ClientDTO.class);
        String formattedRole = userService.formatRole(u.getRoles().get(0).getName());
        dto.setRole(formattedRole);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/getClients",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<List<ClientDTO>> getClients(Pageable pageable,Principal principal) {
        System.out.print(pageable);
        ArrayList<ClientDTO> clientDTOS = new ArrayList<>();
        HttpHeaders header = new HttpHeaders();
        header.setAccessControlExposeHeaders(Collections.singletonList("Total-items"));
        List<Client> clients = clientService.getClients(pageable,header);
        for(Client c: clients){
            ClientDTO dto = mapper.map(c,ClientDTO.class);
            dto.setRole("Client");
            clientDTOS.add(dto);
        }
        System.out.print(header);
        return new ResponseEntity<>(clientDTOS, header,HttpStatus.OK);
    }
    @GetMapping(value = "/getDrivers",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<List<ClientDTO>> getDrivers(Pageable pageable,Principal principal) {
        ArrayList<ClientDTO> driverDTOS = new ArrayList<>();
        HttpHeaders header = new HttpHeaders();
        header.setAccessControlExposeHeaders(Collections.singletonList("Total-items"));
        List<Driver> drivers = driverService.getDrivers(pageable,header);
        for(Driver c: drivers){
            ClientDTO dto = mapper.map(c,ClientDTO.class);
            dto.setRole("Driver");
            driverDTOS.add(dto);
        }
        return new ResponseEntity<>(driverDTOS,header, HttpStatus.OK);
    }

    @PostMapping("/saveClient")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<String> saveClient(@RequestBody ClientDTO clientDTO) {
        Client client = mapper.map(clientDTO,Client.class);
        clientService.setRole(client);
        clientService.saveClient(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/saveDriver")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<String> saveDriver(@RequestBody DriverDTO driverDTO) {
        Driver driver = mapper.map(driverDTO,Driver.class);
        driverService.save(driver);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<String> changePassword(@RequestBody String newPass, Principal principal) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Client u = clientService.getClientByEmail(principal.getName());
        u.setPassword(passwordEncoder.encode(newPass));
        clientService.saveClient(u);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/checkOldPassword")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<Boolean> isOldPasswordCorrect(@RequestBody String oldPass, Principal principal) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Client u = clientService.getClientByEmail(principal.getName());
        if (passwordEncoder.matches(oldPass, u.getPassword())){
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<>(false,HttpStatus.OK);
    }

    @GetMapping("/sendPasswordReset/{email}")
    @PreAuthorize("hasAnyRole('client', 'driver')")
    public ResponseEntity<ClientDTO> sendPasswordReset(@PathVariable String email) {
        User u = userService.findByEmail(email);
        try {
            emailService.sendPasswordChange(u);
        } catch (MessagingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/getDriverPosition/{username}")
    public ResponseEntity<String> getDriverPosition(@PathVariable int username) {
        // NEAKTIVAN ? vrati null
        // AKTIVAN ?
        // nadji trenutnu voznju, pa onda rutu te voznje
        return null;
    }

}

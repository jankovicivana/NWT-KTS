package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.ClientDTO;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.User;
import nvt.kts.project.service.ClientService;
import nvt.kts.project.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ModelMapper mapper;


    @GetMapping("/getUser/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if(user == null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/getClient")
    @PreAuthorize("hasRole('Client')")
    public ResponseEntity<ClientDTO> getUser() {
        Client u = clientService.getClientByEmail("ivanaj0610@gmail.com");
        ClientDTO dto = mapper.map(u,ClientDTO.class);
        String formattedRole = userService.formatRole(u.getRoles().get(0).getName());
        dto.setRole(formattedRole);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/saveClient")
    public ResponseEntity<String> saveClient(@RequestBody ClientDTO clientDTO) {
        Client client = mapper.map(clientDTO,Client.class);
        clientService.setRole(client);
        clientService.saveClient(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody String newPass) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Client u = clientService.getClientByEmail("ivanaj0610@gmail.com");
        u.setPassword(passwordEncoder.encode(newPass));
        clientService.saveClient(u);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/checkOldPassword")
    public ResponseEntity<Boolean> isOldPasswordCorrect(@RequestBody String oldPass) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Client u = clientService.getClientByEmail("ivanaj0610@gmail.com");
        if (passwordEncoder.matches(oldPass, u.getPassword())){
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<>(false,HttpStatus.OK);
    }

   

}

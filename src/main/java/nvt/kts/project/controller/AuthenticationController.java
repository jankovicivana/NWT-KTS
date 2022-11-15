package nvt.kts.project.controller;

import nvt.kts.project.dto.JwtAuthenticationRequest;
import nvt.kts.project.dto.UserRequest;
import nvt.kts.project.dto.UserTokenState;
import nvt.kts.project.model.Client;
import nvt.kts.project.model.Role;
import nvt.kts.project.model.User;
import nvt.kts.project.exception.ResourceConflictException;
import nvt.kts.project.service.UserService;
import nvt.kts.project.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("Lozinkaaaaaaa: " + passwordEncoder.encode("pass"));
        Authentication authentication;
        try{
            System.out.println(authenticationRequest.getUsername());
            System.out.println(authenticationRequest.getPassword());
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch(InternalAuthenticationServiceException e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (DisabledException e){
            return  ResponseEntity.ok(null);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        Role role = user.getRoles().get(0);

        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, role.getName(),user.getEmail()));
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest) throws MessagingException {

        UserDetails existUser = this.userService.loadUserByUsername(userRequest.getEmail());

        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Username already exists"); // vrati bad request
        }
        // treba dodati i za ostale u zavisnosti od uloge
        /*if(userRequest.getRole().equals("ROLE_client")){
            Client client = this.clientService.save(userRequest);
            System.out.println("Maaaaaaaaailll: " + client.getEmail());
            emailService.sendRegistrationActivation(client);
            return new ResponseEntity<>(client, HttpStatus.CREATED);
        }else if(userRequest.getRole().equals("ROLE_cottageOwner")){
            CottageOwner cottageOwner = this.cottageOwnerService.save(userRequest);
            return new ResponseEntity<>(cottageOwner, HttpStatus.CREATED);
        }else if(userRequest.getRole().equals("ROLE_boatOwner")){
            BoatOwner boatOwner = this.boatOwnerService.save(userRequest);
            return new ResponseEntity<>(boatOwner, HttpStatus.CREATED);
        }else if(userRequest.getRole().equals("ROLE_fishingInstructor")){
            FishingInstructor fishingInstructor = this.fishingInstructorService.save(userRequest);
            return new ResponseEntity<>(fishingInstructor, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }*/
            return new ResponseEntity<>(HttpStatus.OK);
    }
}

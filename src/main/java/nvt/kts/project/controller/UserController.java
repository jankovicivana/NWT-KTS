package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    @GetMapping("/getUser")
    public ResponseEntity<String> getUser() {
        System.out.println("super");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

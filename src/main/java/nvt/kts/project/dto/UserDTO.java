package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private String city;
    private String photo;
}

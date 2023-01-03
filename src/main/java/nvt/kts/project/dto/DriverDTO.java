package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DriverDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private String city;
    private String photo;
    private Boolean enabled;
    private Boolean blocked;
    private Boolean isSocialLogin;
}

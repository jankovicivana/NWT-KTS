package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverCarDTO {
    private Long id;
    private Long driverId;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private String city;
    private String photo;
    private Boolean isSocialLogin;
    private String type;
    private Boolean babiesAllowed;
    private Boolean petFriendly;
}

package nvt.kts.project.dto;

import lombok.Data;

@Data
public class UserRequest {

    private Long id;

    private String email;

    private String password;

    private String name;

    private String surname;

    private String city;

    private String cardNumber;

    private String role;

    private String phoneNumber;

    private String photo;


}

package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageClientDTO {

    private String name;
    private String surname;
    private String email;
    private String photo;
    private Boolean isSocialLogin;

    public MessageClientDTO(){}

    public MessageClientDTO(String name, String surname, String email, String photo,Boolean isSocialLogin) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.photo = photo;
        this.isSocialLogin = isSocialLogin;
    }
}

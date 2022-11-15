package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTokenState {

    private String accessToken;
    private Long expiresIn;
    private String role;
    private String userEmail;


    public UserTokenState(String accessToken, long expiresIn, String role, String userEmail) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.role = role;
        this.userEmail = userEmail;
    }

}

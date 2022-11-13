package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTokenState {

    private String accessToken;
    private Long expiresIn;
    private String role;


    public UserTokenState(String accessToken, long expiresIn, String role) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.role = role;
    }

}

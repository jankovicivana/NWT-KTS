package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;
import nvt.kts.project.model.Driver;

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
    private PositionDTO position;

    public DriverDTO(){}

    public DriverDTO(DriverCarDTO driverCarDto,Driver d){
        this.id = driverCarDto.getDriverId();
        this.name = driverCarDto.getName();
        this.surname = driverCarDto.getSurname();
        this.email = driverCarDto.getEmail();
        this.password = driverCarDto.getPassword();
        this.phoneNumber = driverCarDto.getPhoneNumber();
        this.role = driverCarDto.getRole();
        this.city = driverCarDto.getCity();
        this.photo = driverCarDto.getPhoto();
        this.enabled = d.isEnabled();
        this.blocked = d.isBlocked();
        this.isSocialLogin = d.getIsSocialLogin();
        this.position = new PositionDTO(d.getPosition());
    }
}

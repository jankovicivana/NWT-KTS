package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDriveDTO {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Double price;
    private String startPlace = "Beograd";
    private String endPlace = "Trebinje";

    public ClientDriveDTO(Long id, String name, String surname, String email, Double price) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.price = price;
    }

    public ClientDriveDTO(){}
}

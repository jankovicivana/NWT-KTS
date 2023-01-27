package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarTypeDTO {
    private Long id;
    private String type;
    private double price;
    private int personNum;
    private String photo;
}

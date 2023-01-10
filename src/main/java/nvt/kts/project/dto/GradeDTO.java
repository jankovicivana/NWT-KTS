package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeDTO {

    private long id;
    private double driverGrade;
    private double carGrade;
    private String comment;
    private long driverId;
    private String driverName;
    private String driverSurname;
    private long driveId;
    private String carType;

    public GradeDTO(){
        this.id = -1;
        this.driverGrade = 0;
        this.carGrade = 0;
        this.carType = "Honda";
    }

}

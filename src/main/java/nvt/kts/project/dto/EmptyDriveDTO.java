package nvt.kts.project.dto;

import lombok.Getter;
import lombok.Setter;
import nvt.kts.project.model.Drive;

@Getter
@Setter
public class EmptyDriveDTO {

    private Drive drive;
    private double duration;

    public EmptyDriveDTO(){}
}

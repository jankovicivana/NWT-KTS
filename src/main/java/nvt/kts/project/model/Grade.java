package nvt.kts.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "carGrade")
    private double carGrade;

    @Column(name = "driverGrade")
    private double driverGrade;

    @Column(name = "driveId")
    private long driveId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "client")
    private long clientId;

    @Column(name = "driver")
    private long driverId;
}

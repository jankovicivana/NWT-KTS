package nvt.kts.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "admin")
    private Long adminId;

    @JoinColumn(name = "driver")
    private Long driverId;

    @JoinColumn(name = "client")
    private Long clientId;

    @Column(name = "note", nullable = false)
    private String comment;

}

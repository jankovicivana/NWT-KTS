package nvt.kts.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "drives")
public class Drive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver")
    private Driver driver;

    @OneToMany(mappedBy = "drive", fetch = FetchType.LAZY)
    private Set<ClientDrive> passengers = new HashSet<>();

    @OneToMany(mappedBy = "drive", fetch = FetchType.LAZY)
    private Set<Route> routes =  new HashSet<>();

    @Column(name = "startTime")
    private LocalDateTime startTime;

    @Column(name = "endTime")
    private LocalDateTime endTime;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation")
    private Reservation reservation;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "status", nullable = false)
    private DriveStatus status;

    @Column(name = "rejectionReason")
    private String rejectionReason;

}

package nvt.kts.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client")
    private Client client;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "drive")
    private Drive drive;

    @Column(name = "reason", nullable = false)
    private NotificationReason reason;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;



}

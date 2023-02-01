package nvt.kts.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "clientDrives")
public class ClientDrive {
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

    @Column(name = "price")
    private double price;

    @Column(name = "approved")
    private boolean approved = false;

    @Column(name = "favourite")
    private boolean favourite = false;


}

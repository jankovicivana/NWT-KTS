package nvt.kts.project.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "driver", nullable = true)
    protected Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type")
    private CarType type;

    @Column(name = "petFriendly", nullable = false)
    private boolean petFriendly;

    @Column(name = "babiesAllowed", nullable = false)
    private boolean babiesAllowed;
}

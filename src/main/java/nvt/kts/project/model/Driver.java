package nvt.kts.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "drivers")
public class Driver extends User{

    @Column(name = "active", nullable = false)
    private boolean active = false;

    @Column(name = "available", nullable = false)
    private boolean available = false;

    @Column(name = "photo", nullable = false)
    private String photo;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "car")
    protected Car car;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<Drive> drives = new HashSet<>();

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private Set<DriverActivity> activity = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "position")
    private Position position;

}

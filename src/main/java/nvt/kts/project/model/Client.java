package nvt.kts.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "clients")
public class Client extends User{

    @Column(name = "tokens", nullable = false)
    private double tokens;

    @Column(name = "photo", nullable = false)
    private String photo = "unknown.jog";
//
//    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
//    private Set<Route> favouriteRoutes = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<ClientDrive> drives = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<Report> reports = new HashSet<>();

}

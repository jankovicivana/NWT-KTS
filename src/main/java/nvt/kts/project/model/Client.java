package nvt.kts.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "clients")
public class Client extends User{

    @Column(name = "cardNumber", nullable = false)
    private String cardNumber;

    @Column(name = "blocked", nullable = false)
    private boolean blocked;

    @Column(name = "photo", nullable = false)
    private String photo;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<Route> favouriteRoutes = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<ClientDrive> drives = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<Report> reports = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<Grade> grades = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<Note> notes = new HashSet<>();

}

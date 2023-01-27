package nvt.kts.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "editDriver")
public class EditDriver {

    @Id
    @SequenceGenerator(name = "editSeqGen", sequenceName = "editSeq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "editSeqGen")
    protected Long id;

    @Column(name = "driverId", nullable = false)
    private Long driverId;

    @Column(name = "photo", nullable = false)
    private String photo;

    @Column(name = "type")
    private String type;

    @Column(name = "petFriendly", nullable = false)
    private boolean petFriendly;

    @Column(name = "babiesAllowed", nullable = false)
    private boolean babiesAllowed;

    @Column(name = "email", nullable = false)
    protected String email;

    @Column(name = "password", nullable = false)
    protected String password;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "surname", nullable = false)
    protected String surname;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "status", nullable = false)
    private RequestStatus status;



}


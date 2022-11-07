package nvt.kts.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Getter
@Setter
@Entity
@Table(name="users")
@Inheritance(strategy=TABLE_PER_CLASS)
public class User {

    @Id
    @SequenceGenerator(name = "userSeqGen", sequenceName = "userSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeqGen")
    protected Long id;

    @Column(name = "email", unique = true, nullable = false)
    protected String email;

    @Column(name = "password", nullable = false)
    protected String password;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "surname", nullable = false)
    protected String surname;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "city", nullable = false)
    private String city;
}

package nvt.kts.project.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "admins")
public class Admin extends User{

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private Set<Note> notes = new HashSet<>();

}

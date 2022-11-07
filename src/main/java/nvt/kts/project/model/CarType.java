package nvt.kts.project.model;

import javax.persistence.Column;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "carTypes")
public class CarType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "price")
    private double price;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private Set<Car> cars = new HashSet<>();
}

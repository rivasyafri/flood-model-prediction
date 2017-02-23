package com.mofp.model.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "district")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="district_id_seq")
    @SequenceGenerator(name="district_id_seq", sequenceName="district_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column
    @Getter @Setter
    private Geometry area;

    @Column(nullable = false)
    @Getter @Setter
    private String name;

    @Column
    @Getter @Setter
    private String state;

    @Column(nullable = false)
    @Getter @Setter
    private String country;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district", cascade = CascadeType.ALL)
    @Getter @Setter
    @JsonManagedReference
    private List<Weather> weathers;

    @Override
    public String toString() {
        return name + ',' + country;
    }
}

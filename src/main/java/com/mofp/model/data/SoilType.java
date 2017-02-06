package com.mofp.model.data;

import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "soil_type")
public class SoilType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="soil_type_id_seq")
    @SequenceGenerator(name="soil_type_id_seq", sequenceName="soil_type_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private Geometry area;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Getter @Setter
    private SoilTypeRef soilTypeRef;
}

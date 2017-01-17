package com.mofp.framework.model;

import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "drainage")
public class Drainage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="drainage_id_seq")
    @SequenceGenerator(name="drainage_id_seq", sequenceName="drainage_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private Geometry area;

    @Column(nullable = false)
    @Getter @Setter
    private Double discharge;

    @Column
    @Getter @Setter
    private String type;
}

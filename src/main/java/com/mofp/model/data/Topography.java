package com.mofp.model.data;

import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "topography")
public class Topography {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="topography_id_seq")
    @SequenceGenerator(name="topography_id_seq", sequenceName="topography_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private Geometry area;

    @Column(nullable = false)
    @Getter @Setter
    private Double height;
}

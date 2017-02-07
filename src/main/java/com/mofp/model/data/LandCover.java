package com.mofp.model.data;

import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "land_cover")
public class LandCover {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="land_cover_id_seq")
    @SequenceGenerator(name="land_cover_id_seq", sequenceName="land_cover_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private Geometry area;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Getter @Setter
    private LandCoverRef landCoverRef;
}

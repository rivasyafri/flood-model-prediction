package com.mofp.framework.model;

import lombok.Getter;
import lombok.Setter;
import org.postgresql.geometric.PGpolygon;

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
    private PGpolygon area;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Getter @Setter
    private LandCoverRef landCoverRef;
}

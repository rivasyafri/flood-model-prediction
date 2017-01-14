package com.mofp.framework.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "land_cover_ref")
public class LandCoverRef {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="land_cover_ref_id_seq")
    @SequenceGenerator(name="land_cover_ref_id_seq", sequenceName="land_cover_ref_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private Double initialInfiltrationCapacity;

    @Column(nullable = false)
    @Getter @Setter
    private Double constantInfiltrationCapacity;

    @Column(nullable = false)
    @Getter @Setter
    private Double valueK;

    @Column
    @Getter @Setter
    private Double LAI;

    @Column
    @Getter @Setter
    private Double initialwaterBalance;

    @Column
    @Getter @Setter
    private Double numerator;

    @Column
    @Getter @Setter
    private Double denominator;
}

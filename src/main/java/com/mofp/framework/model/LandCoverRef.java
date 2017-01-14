package com.mofp.framework.model;

import lombok.Getter;

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
    @Getter
    private Long id;

    @Column(nullable = false)
    @Getter
    private Double initialInfiltrationCapacity;

    @Column(nullable = false)
    @Getter
    private Double constantInfiltrationCapacity;

    @Column(nullable = false)
    @Getter
    private Double valueK;

    @Column
    @Getter
    private Double LAI;

    @Column
    @Getter
    private Double initialwaterBalance;

    @Column
    @Getter
    private Double numerator;

    @Column
    @Getter
    private Double denominator;
}

package com.mofp.framework.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "soil_type_ref")
public class SoilTypeRef {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="soil_type_ref_id_seq")
    @SequenceGenerator(name="soil_type_ref_id_seq", sequenceName="soil_type_ref_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private Double initialInfiltrationCapacity;

    @Column(nullable = false)
    @Getter @Setter
    private Double constantInfiltrationCapacity;

    @Column
    @Getter @Setter
    private Double initialWaterBalance;

    @Column
    @Getter @Setter
    private Double hidraulicConductivity;

    @Column
    @Getter @Setter
    private Double waterBalanceResidue;

    @Column
    @Getter @Setter
    private Double waterSuctionHead;

    @Column
    @Getter @Setter
    private int soilForm;
}

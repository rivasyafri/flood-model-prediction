package com.mofp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "variable")
public class Variable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="variable_id_seq")
    @SequenceGenerator(name="variable_id_seq", sequenceName="variable_id_seq", allocationSize=1)
    @Getter
    @Setter
    private Long id;

    @Column
    @Getter @Setter
    private boolean usingDrainage = false;

    @Column
    @Getter @Setter
    private double drainageValue = 0;

    @Column
    @Getter @Setter
    private boolean usingEvapotranspiration = false;

    @Column
    @Getter @Setter
    private boolean evapotranspirationByData = false;

    @Column
    @Getter @Setter
    private double evapotranspirationValue = 0;

    @Column
    @Getter @Setter
    private double radiation = 0;

    @Column
    @Getter @Setter
    private double geothermal = 0;

    @Column
    @Getter @Setter
    private double delta = 0;

    @Column
    @Getter @Setter
    private double cn = 0;

    @Column
    @Getter @Setter
    private double cd = 0;

    @Column
    @Getter @Setter
    private double saturatedWaterVapor = 0;

    @Column
    @Getter @Setter
    private double waterVapor = 0;

    @Column
    @Getter @Setter
    private double windSpeed = 0;

    @Column
    @Getter @Setter
    private double meanTemperature = 0;

    @Column
    @Getter @Setter
    private double psychometric = 0;

    @OneToOne(mappedBy = "variable", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Getter @Setter
    private Project project;
}

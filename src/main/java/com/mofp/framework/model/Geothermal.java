package com.mofp.framework.model;

import com.mofp.framework.model.support.AbstractDistrictAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "geothermal")
public class Geothermal extends AbstractDistrictAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="geothermal_id_seq")
    @SequenceGenerator(name="geothermal_id_seq", sequenceName="geothermal_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

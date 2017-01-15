package com.mofp.framework.model;

import com.mofp.framework.model.support.AbstractDistrictAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "temperature")
public class Temperature extends AbstractDistrictAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="temperature_id_seq")
    @SequenceGenerator(name="temperature_id_seq", sequenceName="temperature_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

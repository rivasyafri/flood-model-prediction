package com.mofp.model.data;

import com.mofp.model.support.AbstractDistrictAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "water_vapor")
public class WaterVapor extends AbstractDistrictAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="water_vapor_id_seq")
    @SequenceGenerator(name="water_vapor_id_seq", sequenceName="water_vapor_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

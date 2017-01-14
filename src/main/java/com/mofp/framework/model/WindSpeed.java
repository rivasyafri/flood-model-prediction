package com.mofp.framework.model;

import com.mofp.framework.model.support.AbstractDistrictAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "wind_speed")
public class WindSpeed extends AbstractDistrictAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="wind_speed_id_seq")
    @SequenceGenerator(name="wind_speed_id_seq", sequenceName="wind_speed_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

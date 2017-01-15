package com.mofp.framework.model;

import com.mofp.framework.model.support.AbstractDistrictAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "relative_humidity")
public class RelativeHumidity extends AbstractDistrictAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="relative_humidity_id_seq")
    @SequenceGenerator(name="relative_humidity_id_seq", sequenceName="relative_humidity_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

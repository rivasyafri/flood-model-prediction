package com.mofp.model.data;

import com.mofp.model.support.AbstractDistrictAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "radiation")
public class Radiation extends AbstractDistrictAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="radiation_id_seq")
    @SequenceGenerator(name="radiation_id_seq", sequenceName="radiation_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

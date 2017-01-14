package com.mofp.framework.model;

import com.mofp.framework.model.support.AbstractDistrictAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "precipitation")
public class Precipitation extends AbstractDistrictAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="precipitation_id_seq")
    @SequenceGenerator(name="precipitation_id_seq", sequenceName="precipitation_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

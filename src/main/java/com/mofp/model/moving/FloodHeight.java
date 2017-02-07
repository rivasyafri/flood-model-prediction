package com.mofp.model.moving;

import com.mofp.model.support.AbstractFloodAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "flood_height")
public class FloodHeight extends AbstractFloodAttribute<Double> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="flood_height_id_seq")
    @SequenceGenerator(name="flood_height_id_seq", sequenceName="flood_height_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

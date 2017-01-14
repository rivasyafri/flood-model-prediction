package com.mofp.flood.model;

import com.mofp.flood.model.support.AbstractFloodAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "flood_volume")
public class FloodVolume extends AbstractFloodAttribute<Double> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="flood_volume_id_seq")
    @SequenceGenerator(name="flood_volume_id_seq", sequenceName="flood_volume_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

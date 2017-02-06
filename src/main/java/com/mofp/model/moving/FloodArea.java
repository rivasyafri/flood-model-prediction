package com.mofp.model.moving;

import com.mofp.model.support.AbstractFloodAttribute;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "flood_area")
public class FloodArea extends AbstractFloodAttribute<Geometry> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="flood_area_id_seq")
    @SequenceGenerator(name="flood_area_id_seq", sequenceName="flood_area_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

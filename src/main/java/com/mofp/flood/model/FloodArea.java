package com.mofp.flood.model;

import com.mofp.flood.model.support.AbstractFloodAttribute;
import lombok.Getter;
import lombok.Setter;
import org.postgresql.geometric.PGpolygon;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "flood_area")
public class FloodArea extends AbstractFloodAttribute<PGpolygon> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="flood_area_id_seq")
    @SequenceGenerator(name="flood_area_id_seq", sequenceName="flood_area_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

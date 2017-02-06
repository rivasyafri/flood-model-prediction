package com.mofp.model;

import com.mofp.model.moving.FloodArea;
import com.mofp.model.moving.FloodHeight;
import com.mofp.model.moving.FloodVolume;
import com.mofp.model.support.AbstractProjectAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "flood")
public class Flood extends AbstractProjectAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="flood_id_seq")
    @SequenceGenerator(name="flood_id_seq", sequenceName="flood_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flood", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<FloodArea> catchmentAreas;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flood", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<FloodHeight> floodHeights;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flood", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<FloodVolume> floodVolumes;
}

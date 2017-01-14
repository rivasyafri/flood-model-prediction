package com.mofp.flood.model;

import com.mofp.flood.model.support.AbstractProjectAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

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
    private Set<FloodArea> catchmentAreaSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flood", cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<FloodHeight> floodHeightSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flood", cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<FloodVolume> floodVolumeSet;
}

package com.mofp.model.data;

import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;

import javax.persistence.*;
import java.util.Set;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "district")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="district_id_seq")
    @SequenceGenerator(name="district_id_seq", sequenceName="district_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private Geometry area;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district", cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<WindSpeed> windSpeedSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district", cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<RelativeHumidity> relativeHumiditySet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district", cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<Temperature> temperatureSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district", cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<Geothermal> geothermalSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district", cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<Precipitation> precipitationSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district", cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<Radiation> radiationSet;
}

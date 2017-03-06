package com.mofp.model.data;

import com.mofp.model.support.AbstractDistrictAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "weather_data")
public class Weather extends AbstractDistrictAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="weather_data_id_seq")
    @SequenceGenerator(name="weather_data_id_seq", sequenceName="weather_data_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column
    @Getter
    @Setter
    private Double geothermal;

    @Column
    @Getter
    @Setter
    private Double maxTemperature;

    @Column
    @Getter
    @Setter
    private Double minTemperature;

    @Column
    @Getter
    @Setter
    private Double rain;

    @Column
    @Getter
    @Setter
    private Double radiation;

    @Column
    @Getter
    @Setter
    private Double relativeHumidity;

    @Column
    @Getter
    @Setter
    private Double waterVapor;

    @Column
    @Getter
    @Setter
    private Double windSpeed;

    @Column
    @Getter
    @Setter
    private Double windDegree;

}

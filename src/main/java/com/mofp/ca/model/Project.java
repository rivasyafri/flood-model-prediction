package com.mofp.ca.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mofp.ca.model.json.PolygonToGeoJSON;
import com.mofp.flood.model.Flood;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.common.dataformats.json.jackson.GeometryDeserializer;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Saving the project configuration
 *
 * @author rivasyafri
 */
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="project_id_seq")
    @SequenceGenerator(name="project_id_seq", sequenceName="project_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false, length = 255)
    @Getter @Setter
    private String name;

    @Column
    @Getter @Setter
    @JsonSerialize(using = PolygonToGeoJSON.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Polygon<G2D> area;

    @Column
    @Getter @Setter
    private String description;

    @Column
    @Getter @Setter
    private Integer cellSize;

    @Column
    @Getter @Setter
    private Integer timeStep;

    @Column
    @Getter @Setter
    private Date startDate;

    @Column
    @Getter @Setter
    private Integer interval;

    @Column(nullable = false)
    @Getter @Setter
    private boolean done;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Cell> cellList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Flood> floodList;
}

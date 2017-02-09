package com.mofp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mofp.model.moving.CellHeightWater;
import com.mofp.model.moving.CellState;
import com.mofp.model.support.json.CellSerializer;
import com.mofp.model.support.json.CellStateSerializer;
import com.mofp.model.support.json.PolygonDeserializer;
import com.mofp.model.support.json.PolygonToGeoJSON;
import lombok.Getter;
import lombok.Setter;
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

    @Column(nullable = false)
    @Getter @Setter
    private String name;

    @Column
    @Getter @Setter
    @JsonSerialize(using = PolygonToGeoJSON.class)
    @JsonDeserialize(using = PolygonDeserializer.class)
    private Polygon<G2D> area;

    @Column
    @Getter @Setter
    private String description;

    @Column
    @Getter @Setter
    private Integer cellSize = 100;

    @Column
    @Getter @Setter
    private Integer timeStep = 60;

    @Column
    @Getter @Setter
    private Date startTime;

    @Column
    @Getter @Setter
    private Integer interval = 3600;

    @Column(nullable = false)
    @Getter @Setter
    private boolean done = false;

    @Column(nullable = false)
    @Getter @Setter
    private String model = "Prasetya";

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "variableId", nullable = false)
    @Getter @Setter
    private Variable variable;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Flood> floods;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    @JsonIgnore
    private List<CellHeightWater> cellHeightWaters;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<CellState> cellStates;

    public Project() {
        this.variable = new Variable();
    }
}

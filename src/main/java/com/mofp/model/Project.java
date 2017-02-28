package com.mofp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mofp.model.moving.CellHeightWater;
import com.mofp.model.moving.CellState;
import com.mofp.model.support.json.PolygonDeserializer;
import com.mofp.model.support.json.PolygonToGeoJSON;
import com.mofp.service.method.support.FloodModel;
import com.mofp.service.method.support.InundationModel;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.springframework.data.annotation.Transient;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
    private Long startTime;

    @Column
    @Getter @Setter
    private Long endTime;

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
    @JsonIgnore
    @JsonManagedReference
    private List<Cell> cells;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    @JsonIgnore
    private List<CellHeightWater> cellHeightWaters;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<CellState> cellStates;

    ////*********************** Global Variable ******************************** ////
    @Transient
    @JsonIgnore
    public transient Pair<Double, Double> SIZE;
    @Transient
    @JsonIgnore
    public transient Pair<Double, Double> DELTA;
    @Transient
    @JsonIgnore
    public transient Pair<Double, Double> NORTH_WEST;
    @Transient
    @JsonIgnore
    public transient Pair<Integer, Integer> NUMBER_OF_CELL;
    @Transient
    @JsonIgnore
    public transient FloodModel SELECTED_MODEL;
    @Transient
    @JsonIgnore
    public transient InundationModel INUNDATION_MODEL;
    @Transient
    @JsonIgnore
    public transient Cell[][] MATRIX;
    @Transient
    @JsonIgnore
    public transient ArrayList<Cell> PROCESSED_CELLS = new ArrayList<>();
    @Transient
    @JsonIgnore
    public transient PriorityQueue<Cell> ACTIVE_CELLS = new PriorityQueue<>();
    @Transient
    @JsonIgnore
    public transient PriorityQueue<Cell> NEW_ACTIVE_CELLS = new PriorityQueue<>();
    ////*********************** End of Global Variable ************************** ////

    public Project() {
        this.variable = new Variable();
    }
}

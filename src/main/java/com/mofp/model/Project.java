package com.mofp.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mofp.dao.VariableRepository;
import com.mofp.model.support.json.PolygonDeserializer;
import com.mofp.model.support.json.PolygonToGeoJSON;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;

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
    private Integer cellSize;

    @Column
    @Getter @Setter
    private Integer timeStep;

    @Column
    @Getter @Setter
    private Date startTime;

    @Column
    @Getter @Setter
    private Integer interval;

    @Column(nullable = false)
    @Getter @Setter
    private boolean done = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "variableId", nullable = false)
    @Getter @Setter
    private Variable variable;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Cell> cells;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Flood> floods;

    public Project() {
        this.variable = new Variable();
    }
}

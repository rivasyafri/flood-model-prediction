package com.mofp.ca.model;

import com.mofp.flood.model.Flood;
import lombok.Getter;
import lombok.Setter;
import org.postgresql.geometric.PGpolygon;

import javax.persistence.*;
import java.util.List;

/**
 * Saving the project configuration
 *
 * @author taufiq
 * @modifiedBy rivasyafri by adding connection to database via persistence
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
    private PGpolygon areaSimulation;

    @Column(nullable = false)
    @Getter @Setter
    private String description;

    @Column(nullable = false)
    @Getter @Setter
    private int cellSize;

    @Column(nullable = false)
    @Getter @Setter
    private int timeStep;

    @Column(nullable = false)
    @Getter @Setter
    private boolean isDone;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Cell> cellList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Flood> floodList;
}

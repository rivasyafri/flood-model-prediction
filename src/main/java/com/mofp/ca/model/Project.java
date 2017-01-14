package com.mofp.ca.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(nullable = false)
    @Getter @Setter
    private String description;

    @Column(length = 255)
    @Getter @Setter
    private String cellspacePath;

    @Column(length = 255)
    @Getter @Setter
    private String rulePath;

    @Column(nullable = false)
    @Getter @Setter
    private int cellSize;
}

package com.mofp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "state")
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="state_id_seq")
    @SequenceGenerator(name="state_id_seq", sequenceName="state_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false, length = 50)
    @Getter @Setter
    private String name;

    @Column(nullable = false)
    @Getter @Setter
    private boolean active;

    public State(){}

    public State(String name) {
        this.name = name;
        this.active = false;
    }

    public State(String name, boolean active) {
        this.name = name;
        this.active = active;
    }
}

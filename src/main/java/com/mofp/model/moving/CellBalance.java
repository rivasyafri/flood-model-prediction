package com.mofp.model.moving;

import com.mofp.model.support.AbstractCellAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "cell_balance")
public class CellBalance extends AbstractCellAttribute<Double> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cell_balance_id_seq")
    @SequenceGenerator(name="cell_balance_id_seq", sequenceName="cell_balance_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}

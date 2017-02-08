package com.mofp.model.moving;

import com.mofp.model.support.AbstractCellAttribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "cell_height_water")
public class CellHeightWater extends AbstractCellAttribute<Double> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cell_height_water_id_seq")
    @SequenceGenerator(name="cell_height_water_id_seq", sequenceName="cell_height_water_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellHeightWater that = (CellHeightWater) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

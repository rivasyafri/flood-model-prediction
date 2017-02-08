package com.mofp.model.support.json;

import com.mofp.model.Cell;
import com.mofp.model.Project;
import com.mofp.model.State;
import com.mofp.model.Variable;
import com.mofp.model.moving.CellHeightWater;
import com.mofp.model.moving.CellState;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.List;

/**
 * @author rivasyafri
 */
@Projection(name = "inlineCellState", types = {CellState.class})
public interface CellStateToStateProjectAndCellProjection {

    Long getId();

    @Value("#{target.cell.getXArray()}")
    Integer getXArray();

    @Value("#{target.cell.getYArray()}")
    Integer getYArray();

    @Value("#{target.value.getName()}")
    String getName();
    Integer getStartTime();
    Integer getEndTime();
}

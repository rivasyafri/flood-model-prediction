package com.mofp.model.support.json;

import com.mofp.model.Project;
import com.mofp.model.Variable;
import com.mofp.model.moving.CellHeightWater;
import com.mofp.model.moving.CellState;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.List;

/**
 * @author rivasyafri
 */
@Projection(name = "inlineVariable", types = {Project.class})
public interface inlineVariable {

    Long getId();
    String getName();
    Polygon<G2D> getArea();
    String getDescription();
    Integer getCellSize();
    Integer getTimeStep();
    Date getStartTime();
    boolean isDone();
    String getModel();
    Variable getVariable();
    List<CellHeightWater> getCellHeightWaters();
    List<CellState> getCellStates();
}

package com.mofp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mofp.model.data.District;
import com.mofp.model.support.AbstractProjectAttribute;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Polygon;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Random;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "cell")
public class Cell extends AbstractProjectAttribute implements Comparable<Cell> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cell_id_seq")
    @SequenceGenerator(name="cell_id_seq", sequenceName="cell_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    @JsonIgnore
    private Polygon<G2D> area;

    @Getter
    @Setter
    private Integer xArray;

    @Getter
    @Setter
    private Integer yArray;

    @Getter
    @Setter
    private double height = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "districtId")
    @Getter @Setter
    protected District district;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient State currentState;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient double waterHeight = 0;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient double totalHeight;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient double waterBalanceBefore = 0;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient double waterBalanceAfter = 0;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient double constantInfiltrationCapacity = 0;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient double initialInfiltrationCapacity = 0;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient double kValue = 0;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient double waterProofPercentage = 0;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient double psiOrBi = 0;

    @Getter @Setter
    @Transient
    @JsonIgnore
    private transient long timeStartFlooded = 0;

    public Cell() {}

    public Cell(int xArray, int yArray, State state) {
        this.xArray = xArray;
        this.yArray = yArray;
        if (state != null) {
            this.currentState = new State(state.getName());
        } else {
            this.currentState = null;
        }
    }

    public void updateTotalHeight() {
        this.totalHeight = this.height + this.waterHeight;
    }

    @Override
    public int compareTo(Cell o) {
        if (this.totalHeight == o.getTotalHeight()) {
            return 0;
        } else if (this.totalHeight < o.getTotalHeight()) {
            return -1;
        } else {
            return 1;
        }
    }

    public void randomizeData() {
        Random randomGenerator = new Random();
        this.constantInfiltrationCapacity = randomGenerator.nextDouble();
        this.initialInfiltrationCapacity = randomGenerator.nextDouble() * 5 + 10;
        this.kValue = randomGenerator.nextDouble();
        this.psiOrBi = randomGenerator.nextDouble();
        this.waterProofPercentage = randomGenerator.nextDouble();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (id != null ? !id.equals(cell.id) : cell.id != null) return false;
        if (xArray != null ? !xArray.equals(cell.xArray) : cell.xArray != null) return false;
        return yArray != null ? yArray.equals(cell.yArray) : cell.yArray == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (xArray != null ? xArray.hashCode() : 0);
        result = 31 * result + (yArray != null ? yArray.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "id=" + id +
                ", xArray=" + xArray +
                ", yArray=" + yArray +
                '}';
    }

    public HashMap<Integer, Double> getCenterPointOfArea() {
        double lat1, lat2 = 0, lon1, lon2 = 0;
        LineString<G2D> line = area.getExteriorRing();
        lat1 = line.getPositionN(0).getLat();
        lon1 = line.getPositionN(0).getLon();
        for (int i = 1; i < line.getNumPositions() - 1; i++) {
            G2D position = line.getPositionN(i);
            lat2 = lat1 != position.getLat() ? position.getLat() : lat2;
            lon2 = lon1 != position.getLon() ? position.getLon() : lon2;
        }
        double avgLat = (lat1 + lat2) / 2;
        double avgLng = (lon1 + lon2) / 2;
        HashMap<Integer, Double> result = new HashMap<>();
        result.put(1, avgLat);
        result.put(2, avgLng);
        return result;
    }
}

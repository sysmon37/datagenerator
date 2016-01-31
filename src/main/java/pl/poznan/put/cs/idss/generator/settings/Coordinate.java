package pl.poznan.put.cs.idss.generator.settings;

import java.util.List;



public class Coordinate extends NotEmptyDoubleList {
    
    public Coordinate(List<Double> values) {
        super(values);
    }
    
    public Coordinate(Coordinate c) {
        this(c._values);
    }
    
    public Coordinate set(int index, double value) {
        _values.set(index, value);
        return this;
    }
        
    public int getNumDimensions() {
        return size();
    }
}

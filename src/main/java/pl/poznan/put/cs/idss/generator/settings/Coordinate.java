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
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            if (i > 0)
                builder.append(", ");
            builder.append(get(i));
        }
        return builder.toString();
    }
    
    public int getNumDimensions() {
        return size();
    }
}

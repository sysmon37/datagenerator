package config;

import java.util.List;



public class Coordinate extends NotEmptyDoubleList {
    
    public Coordinate(List<Double> values) {
        super(values);
    }
    
    public Coordinate(Coordinate c) {
        this(c._values);
    }
    
}

package pl.poznan.put.cs.idss.generator.settings;

import java.util.List;

/**
 * Stores size (i.e. radius)
 * 
 * @author swilk
 */
public class Size extends NotEmptyDoubleList {
    public Size(List<Double> values) {
        super(values);
    }
    
    public Size(Size s) {
       this(s._values);
    }
    
    public int getNumDimensions() {
        return size();
    }
}

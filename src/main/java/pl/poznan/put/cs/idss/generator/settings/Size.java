package pl.poznan.put.cs.idss.generator.settings;

import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.Validate;

/**
 * Stores size (i.e. radius)
 * 
 * @author swilk
 */
@EqualsAndHashCode(callSuper=true)
public class Size extends NotEmptyDoubleList {
    public Size(List<Double> values) {
        super(values);
        Validate.isTrue(values.stream().allMatch(d -> d >= 0));
    }
    
    public Size(Size s) {
       this(s._values);
    }
    
    public Size(int n, double v) {
        this(Collections.nCopies(n, v));
    }
    
    public int getNumDimensions() {
        return size();
    }
    
    public boolean isZero() {
        return _values.stream().allMatch(d -> d == 0);
    }
    
}

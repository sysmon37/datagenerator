package pl.poznan.put.cs.idss.generator.settings;

import lombok.Data;

/**
 *
 * @author swilk
 */
@Data
public class Distribution {
    final private DistributionType _type;
    private double _numStandardDeviations = 1.0;  
    
    @Override 
    public String toString() {
        if (_type == DistributionType.NORMAL)
            return String.format("%s [%g]", _type, _numStandardDeviations);
        else
            return String.format("%s", _type);
    }
}

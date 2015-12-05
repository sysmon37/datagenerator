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
}
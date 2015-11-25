/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package pl.poznan.put.cs.idss.generator.settings;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author swilk
 */
@Data
public class Region {

    private Double _weight = null;
    private Double _borderZone = null;
    private Double _noOutlierZone = null;
    private ShapeType _shape = null;
    private Coordinate _center = null;
    private Size _radius = null;
    private Distribution _distribution = null;
    private List<Rotation> _rotations = null;
    
    private Ratio[] _exampleTypeDistributions = new Ratio[Ratio.SIZE_LEARN_TEST];

    public Region() {
    }

    public Region(Region r) {
        this(r._weight, r._shape, r._center, r._radius, 
                r._borderZone, r._noOutlierZone, r._distribution, r._rotations);
    }

    public Region(double weight, ShapeType shape, Coordinate center, Size radius, 
            double borderZone, double noOutlierZone, Distribution distribution, List<Rotation> rotations) {
        Validate.isTrue(weight > 0);
        Validate.isTrue(borderZone >= 0);
        Validate.isTrue(noOutlierZone >= 0);
        Validate.isTrue(center != null && radius != null ? center.getNumDimensions() == radius.getNumDimensions() : true);
        
        _weight = weight;
        _shape = shape;
        _center = center;
        _radius = radius;
        _borderZone = borderZone;
        _noOutlierZone = noOutlierZone;
        _distribution = distribution;
        _rotations = rotations != null ? rotations : new ArrayList<>();
    }
    
    /**
     * Returns the number of examples from a given set (learn or test).
     * 
     * @param setIndex -- @see Ratio#LEARN and @see Ratio#TEST
     * @return 
     */
    public int getNumExamples(int setIndex) {
        return (int) _exampleTypeDistributions[setIndex].getTotal();
    }
    
    /**
     * Returns the number of objects from a given set (learn or test) and of given
     * type (safe, border, rare, outlier).
     * 
     * @param setIndex -- Ratio.LEARN or Ratio.TEST
     * @param typeIndex -- Ratio.SAFE, Ratio.BORDER, Ratio.RARE, Ratio.OUTLIER
     * @return 
     */
    public int getNumExamples(int setIndex, int typeIndex) {
        return (int) _exampleTypeDistributions[setIndex].get(typeIndex);
    }
    
    public Region setExampleTypeDistribution(int setIndex, Ratio distribution) {
        _exampleTypeDistributions[setIndex] = new Ratio(distribution);
        return this;
    }
    
    public Ratio getExampleTypeDistribution(int setIndex) {
        return _exampleTypeDistributions[setIndex];
    }
    
    public int getNumRotations() {
        return _rotations.size();
    }
    
    public Rotation getRotation(int rotationIndex) {
        return _rotations.get(rotationIndex);
    }
    
    public Region setRotations(List<Rotation> rotations) {
        Validate.noNullElements(rotations);
        _rotations = rotations;
        return this;
    }

}

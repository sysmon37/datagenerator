package pl.poznan.put.cs.idss.generator.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author swilk
 */
@Data
@ToString(exclude="_decisionClass")
public class Region {
    
    private DecisionClass _decisionClass = null;

    private Double _weight = null;
    private Double _borderZone = null;
    private Double _noOutlierZone = null;
    private ShapeType _shape = null;
    private Coordinate _center = null;
    /**
     * radius -- interpretation is dependent on the border type:
     * - FIXED => safeRadius
     * - AUTO =>  initial radius used for subsequent adjustments (may be different than safe, border and no outlier radiuses)
     */
    private Size _radius = null;
    @Getter
    private Size _safeRadius = null;
    /**
     * borderRadius = safeRadius + borderZone (after adjustment)
     */
    @Getter
    private Size _borderRadius = null;
    
    /**
     * noOutlierRadius = borderRadius + noOutlierZone
     */
    @Getter
    private Size _noOutlierRadius = null;
    
    private Distribution _distribution = null;
    private List<Rotation> _rotations = null;
    private BorderType _border = BorderType.FIXED;
    
    private Ratio[] _exampleTypeDistributions = new Ratio[Ratio.SIZE_LEARN_TEST];

    public Region() {
    }

    public Region(Region r) {
        this(r._weight, r._shape, r._center, r._radius, r._border,
                r._borderZone, r._noOutlierZone, r._distribution, r._rotations);
    }

    public Region(Double weight, ShapeType shape, Coordinate center, Size radius, 
           BorderType border, Double borderZone, Double noOutlierZone, Distribution distribution, List<Rotation> rotations) {
        Validate.isTrue(weight != null ? weight > 0 : true);
        Validate.isTrue(borderZone != null ? borderZone >= 0 : true);
        Validate.isTrue(noOutlierZone != null ? noOutlierZone >= 0 : true);
        Validate.isTrue(center != null && radius != null ? center.getNumDimensions() == radius.getNumDimensions() : true);
        
        _weight = weight;
        _shape = shape;
        _center = center;
        _radius = radius;
        _border = border;
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

   
    /**
     * Updates all the radiuses (safe, border and noOutlier). It needs to be called
     * before a region is "used". Normally there is no need for explicit invocation
     * (it is done by GeneratorSettings), however, this may be necessary when testing
     * the Region class in isolation from other classes.
     * @return this (the current region)
     */
    public Region updateRadiuses() {
        Validate.isTrue(_radius != null);
        if (getBorder() == BorderType.FIXED) {
            Validate.isTrue(_borderZone != null);
            Double borderRadius[] = new Double[_radius.getNumDimensions()];
            for (int i = 0; i < _radius.size(); i++) 
                borderRadius[i] = _radius.get(i) + _borderZone;
            _borderRadius = new Size(Arrays.asList(borderRadius));
            _safeRadius = new Size(_radius);
        } else {
            double safeBorderScalingFactor = 1.0;
            double safeScalingFactor = 0.0;
            if (_decisionClass != null) {
                Ratio exampleTypeRatio = _decisionClass.getExampleTypeRatio();
                if (exampleTypeRatio.getTotal() > 0) {
                    double safeBorder = exampleTypeRatio.get(Ratio.SAFE) + exampleTypeRatio.get(Ratio.BORDER);
                    double safeBorderToOverall = safeBorder/exampleTypeRatio.getTotal();
                    safeBorderScalingFactor = Math.pow(safeBorderToOverall, 1.0/_radius.getNumDimensions());
                    if (safeBorder > 0.0) {
                        double safeToBorder = exampleTypeRatio.get(Ratio.SAFE)/safeBorder; 
                        safeScalingFactor = Math.pow(safeToBorder, 1.0/_radius.getNumDimensions());
                    }                    
                }                
            }
            Double safeRadius[] = new Double[_radius.getNumDimensions()];
            Double borderRadius[] = new Double[_radius.getNumDimensions()];
            for (int i = 0; i < _radius.size(); i++)  {
                borderRadius[i] = _radius.get(i) * safeBorderScalingFactor;
                 safeRadius[i] = borderRadius[i] * safeScalingFactor;
            }
            _safeRadius = new Size(Arrays.asList(safeRadius));
            _borderRadius = new Size(Arrays.asList(borderRadius));
        }
        Double noOutlierRadius[] = new Double[_radius.getNumDimensions()];
        for (int i = 0; i < _radius.size(); i++)
            noOutlierRadius[i] = _borderRadius.get(i) + _noOutlierZone;
        _noOutlierRadius = new Size(Arrays.asList(noOutlierRadius));
        return this;
    }
    
    public Size getRadius(RangeType range) {
        switch (range) {
            case SAFE: return getSafeRadius();
            case BORDER : return getBorderRadius();
            default: return getNoOutlierRadius();
        }
    }

    public Region setDecisionClass(DecisionClass clazz) {
        Validate.notNull(clazz);
        _decisionClass = clazz;
        return this;
    }

    public Region setWeight(Double weight) {
        Validate.isTrue(weight != null && weight >= 0);
        _weight = weight;
        return this;
    }
    
    public Region setBorderZone(Double borderZone) {
        Validate.isTrue(borderZone != null && borderZone >= 0);
        _borderZone = borderZone;
        return this;
    }
    
    public Region setNoOutlierZone(Double noOutlierZone) {
        Validate.isTrue(noOutlierZone != null && noOutlierZone >= 0);
        _noOutlierZone = noOutlierZone;
        return this;
    }
    
    public Region setRadius(Size radius) {
        Validate.isTrue(radius != null);
        Validate.isTrue(radius.asList().stream().allMatch((d) -> d != null && d >= 0));
        _radius = new Size(radius);
        return this;
    }
    
    public Region setBorder(BorderType border) {
        Validate.isTrue(border != null);
        _border = border;
        return this;
    }
    
}

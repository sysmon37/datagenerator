/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package config;

import java.util.List;
import lombok.Data;

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

    public Region() {

    }

    public Region(Region r) {
        this(r._weight, r._shape, r._center, r._radius, 
                r._borderZone, r._noOutlierZone, r._distribution, r._rotations);
    }

    public Region(double weight, ShapeType shape, Coordinate center, Size radius, 
            double borderZone, double noOutlierZone, Distribution distribution, List<Rotation> rotations) {
        _weight = weight;
        _shape = shape;
        _center = center;
        _radius = radius;
        _borderZone = borderZone;
        _noOutlierZone = noOutlierZone;
        _distribution = distribution;
        _rotations = rotations;
    }

}

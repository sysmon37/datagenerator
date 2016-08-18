package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Region;
import java.util.List;
import org.apache.commons.lang3.Validate;
import pl.poznan.put.cs.idss.generator.settings.RangeType;
import pl.poznan.put.cs.idss.generator.settings.Size;

public class HyperCircularDataShape extends DataShape {

    private final RandomGenerator _safeGenerator;
    private final RandomGenerator _borderGenerator;

    public HyperCircularDataShape(Region region,
            RandomGenerator safeGenerator,
            RandomGenerator borderGenerator) {
        super(region);
        _safeGenerator = safeGenerator;
        _borderGenerator = borderGenerator;
    }

    @Override
    public Point generateSafePoint() {
        Point point = null;
        do {
            List<Double> coord = _safeGenerator.getNumbers();
            for (int i = 0; i < _region.getCenter().getNumDimensions(); ++i) {
                coord.set(i, coord.get(i) * _region.getSafeRadius().get(i) + _region.getCenter().get(i));
            }

            point = new Point(coord);
        } while (!isCovered(point, RangeType.SAFE));
        return point;
    }

    @Override
    public Point generateBorderPoint() {
        Point point = null;
        do {
            List<Double> coord = _borderGenerator.getNumbers();
            for (int i = 0; i < _region.getCenter().getNumDimensions(); ++i) {
                coord.set(i, coord.get(i) * _region.getBorderRadius().get(i) + _region.getCenter().get(i));
            }
            point = new Point(coord);
        } while (isCovered(point, RangeType.SAFE) || !isCovered(point, RangeType.BORDER));
        return point;
    }

    @Override
    protected boolean isCovered(Point point, Size radius) {
        Validate.isTrue(point.getNumDimensions() == radius.getNumDimensions());
        double distance = 0;
        for (int i = 0; i < point.getNumDimensions(); ++i) {
            distance += Math.pow(point.get(i) - _region.getCenter().get(i), 2)
                    / Math.pow(radius.get(i), 2);
        }
        return distance <= 1;
    }

}

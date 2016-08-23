package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Region;
import java.util.List;
import pl.poznan.put.cs.idss.generator.settings.RangeType;
import pl.poznan.put.cs.idss.generator.settings.Size;

public class HyperRectangularDataShape extends DataShape {

    private final RandomGenerator _safeGenerator;
    private final RandomGenerator _borderGenerator;

    public HyperRectangularDataShape(Region region,
            RandomGenerator safeGenerator,
            RandomGenerator borderGenerator) {
        super(region);
        _safeGenerator = safeGenerator;
        _borderGenerator = borderGenerator;
    }

    @Override
    public Point generateBorderPoint() {
        Point point = null;
        do {
            List<Double> coord = _borderGenerator.getNumbers();
            for (int i = 0; i < _region.getCenter().getNumDimensions(); i++) {
                coord.set(i, coord.get(i) * _region.getBorderRadius().get(i)  + _region.getCenter().get(i));
            }
            point = new Point(coord);
        } while (isCovered(point, RangeType.SAFE));
        return point;
    }

    @Override
    protected boolean isCovered(Point point, Size radius) {
        for (int i = 0; i < point.getNumDimensions(); ++i) {
            if (!isInsideInterval(point.get(i),
                    _region.getCenter().get(i) - radius.get(i),
                    _region.getCenter().get(i) + radius.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isInsideInterval(Double value, double lowerBound, double upperBound) {
        return lowerBound <= value && value <= upperBound;
    }

    @Override
    public Point generateSafePoint() {
        List<Double> coord = _safeGenerator.getNumbers();
        for (int i = 0; i < _region.getCenter().getNumDimensions(); i++) {
            coord.set(i, coord.get(i) * _region.getSafeRadius().get(i) + _region.getCenter().get(i));
        }
        return new Point(coord);
    }

}

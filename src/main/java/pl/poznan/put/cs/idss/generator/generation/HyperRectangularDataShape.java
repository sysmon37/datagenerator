package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Region;
import java.util.ArrayList;
import java.util.List;

public class HyperRectangularDataShape extends DataShape {

    private final RandomGenerator _coreGenerator;
    private final RandomGenerator _overlappingGenerator;

    
    public HyperRectangularDataShape(Region region,
            RandomGenerator coreGenerator,
            RandomGenerator overlappingGenerator) {
        super(region);
        _coreGenerator = coreGenerator;
        _overlappingGenerator = overlappingGenerator;
    }

    @Override
    public Point generateOverlappingPoint() {
        Point point  = null;
        do {
            List<Double> coord = _overlappingGenerator.getNumbers();
            for (int i = 0; i < _region.getCenter().getNumDimensions(); i++) {
				coord.set(i, coord.get(i) * (_region.getRadius().get(i) + _region.getBorderZone()) + _region.getCenter().get(i));
            }
            point = new Point(coord);
        } while (isCovered(point, 0));
        return point;
    }

    @Override
    protected boolean isCovered(Point point, double margin) {
        for (int i = 0; i < point.getNumDimensions(); ++i) {
            if (!isInsideInterval(point.get(i),
                    _region.getCenter().get(i) - _region.getRadius().get(i) - margin,
                    _region.getCenter().get(i) + _region.getRadius().get(i) + margin)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInsideInterval(Double value, double lowerBound, double upperBound) {
        return lowerBound <= value && value <= upperBound;
    }

    @Override
    public Point generateCorePoint() {
        List<Double> coord = _coreGenerator.getNumbers();
        for (int i = 0; i < _region.getCenter().getNumDimensions(); i++) {
			coord.set(i, coord.get(i) * _region.getRadius().get(i) + _region.getCenter().get(i));
        }
        return new Point(coord);
    }

}

package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Region;
import java.util.ArrayList;
import java.util.List;

public class HyperCircularDataShape extends DataShape {

    private final RandomGenerator _coreGenerator;
    private final RandomGenerator _overlappingGenerator;
    
    public HyperCircularDataShape(Region region,
            RandomGenerator coreGenerator,
            RandomGenerator overlappingGenerator) {
        super(region);
        _coreGenerator = coreGenerator;
        _overlappingGenerator = overlappingGenerator;
    }

    @Override
    public Point generateCorePoint() {
        Point point = null;
        do {
            List<Double> coord = new ArrayList<>(_region.getCenter().getNumDimensions());
            for (int i = 0; i < _region.getCenter().getNumDimensions(); ++i) {
                double number = _coreGenerator.getNumber(0, 1);
				coord.add(number * _region.getRadius().get(i) + _region.getCenter().get(i));
            }

            point = new Point(coord);
        } while (!isCovered(point, 0));
        return point;
    }

    @Override
    public Point generateOverlappingPoint() {
        Point point = null;
        do {
            List<Double> coord = new ArrayList<>(_region.getCenter().getNumDimensions());
            for (int i = 0; i < _region.getCenter().getNumDimensions(); ++i) {
                coord.add(_overlappingGenerator.getNumber(_region.getCenter().get(i), _region.getRadius().get(i) + _region.getBorderZone()));
            }
            point = new Point(coord);
        } while (isCovered(point, 0) || !isCovered(point, _region.getBorderZone()));
        return point;
    }

    @Override
    protected boolean isCovered(Point point, double additionalMargin) {
        double distance = 0;
        for (int i = 0; i < point.getNumDimensions(); ++i) {
            distance += Math.pow(point.get(i) - _region.getCenter().get(i), 2)
                    / Math.pow(_region.getRadius().get(i) + additionalMargin, 2);
        }
        return distance <= 1;
    }
}

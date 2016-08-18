package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Region;
import java.util.List;
import pl.poznan.put.cs.idss.generator.settings.Size;

public class IntegumentalDataShape extends DataShape {

    private final DataShape _underlyingDataShape;
    private final List<RegionGenerator> _otherGenerators;
    private int _dataShapeIndex = 0;

    public IntegumentalDataShape(
            Region region,
            RandomGenerator coreGenerator,
            List<RegionGenerator> otherGenerators
    ) {
        super(region);
        this._otherGenerators = otherGenerators;
        _underlyingDataShape = new HyperRectangularDataShape(region,
                coreGenerator,
                null);
    }

    @Override
    public Point generateSafePoint() {
        Point point = null;
        do {
            point = _underlyingDataShape.generateSafePoint();
        } //		while(isCoveredByAnyRegion(point));
        while (isInCoreZoneOfAnyRegion(point));
        return point;
    }

    @SuppressWarnings("unused")
    private boolean isCoveredByAnyRegion(Point point) {
        for (RegionGenerator data : _otherGenerators) {
            if (data.isCovered(point)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInCoreZoneOfAnyRegion(Point point) {
        for (RegionGenerator r : _otherGenerators) {
            if (r.isInSafeRange(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Point generateBorderPoint() {
        Point point = _otherGenerators.get(_dataShapeIndex).generateBorderPoint();
        _dataShapeIndex = (_dataShapeIndex + 1) % _otherGenerators.size();
        return point;
    }


    @Override
    protected boolean isCovered(Point point, Size margin) {
        return false;
    }
}

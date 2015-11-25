package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Region;
import java.util.List;

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
    public Point generateCorePoint() {
        Point point = null;
        do {
            point = _underlyingDataShape.generateCorePoint();
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
            if (r.isInCoreZone(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Point generateOverlappingPoint() {
        Point point = _otherGenerators.get(_dataShapeIndex).generateOverlappingPoint();
        _dataShapeIndex = (_dataShapeIndex + 1) % _otherGenerators.size();
        return point;
    }


    @Override
    protected boolean isCovered(Point point, double margin) {
        return false;
    }
}

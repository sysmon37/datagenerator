package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Coordinate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;

public class OutlierFirstPointGenerator implements PointGenerator {

    private List<Double> _means;
    private List<Double> _deviations;
    private final RandomGenerator _generator;

    public OutlierFirstPointGenerator(Coordinate minCoordinate,
            Coordinate maxCoordinate,
            RandomGenerator generator) {
        Validate.isTrue(minCoordinate != null);
        Validate.isTrue(maxCoordinate != null);
        Validate.isTrue(minCoordinate.getNumDimensions()== maxCoordinate.getNumDimensions());
        _generator = generator;
        calculateBounds(minCoordinate, maxCoordinate);
    }

    private void calculateBounds(Coordinate minCoordinate, Coordinate maxCoordinate) {
        
        _means = new ArrayList<>();
        _deviations = new ArrayList<>();
        for (int i = 0; i < minCoordinate.getNumDimensions(); ++i) {
            double min = minCoordinate.get(i);
            double max = maxCoordinate.get(i);

            Validate.isTrue(min <= max, String.format("Min value %f exceeds max value %f", min, max));

            double range = (max - min);
            _means.add(min + range / 2);
            _deviations.add(range / 2);
        }
    }

    @Override
    public Point generate() {
        List<Double> coord = _generator.getNumbers();
        for (int i = 0; i < _means.size(); ++i) {
			coord.set(i, coord.get(i) * _deviations.get(i) + _means.get(i));
        }
        return new Point(coord);
    }
}

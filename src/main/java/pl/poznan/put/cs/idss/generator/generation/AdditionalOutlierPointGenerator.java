package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
import java.util.List;

public class AdditionalOutlierPointGenerator implements PointGenerator {

    private Point middlePoint;
    private static final int K_STANDARD_DEVIATION = 10;
    private RandomGenerator generator;
    private List<Double> deviations;

    public AdditionalOutlierPointGenerator(Point middlePoint,
            NearestNeighbourSelector selector,
            NeighbourhoodStandardDeviationCalculator deviationCalculator,
            RandomGenerator generator,
            List<Example> examples) {
        this.middlePoint = middlePoint;
        this.generator = generator;
        deviations = getStandardDeviations(examples, selector, deviationCalculator);
    }

    private List<Double> getStandardDeviations(List<Example> examples,
            NearestNeighbourSelector selector,
            NeighbourhoodStandardDeviationCalculator deviationCalculator) {
        List<Example> nearest = selector.getNeighbours(K_STANDARD_DEVIATION, middlePoint, examples);
        return deviationCalculator.calculateStandardDeviations(nearest);
    }

    public Point generate() {
        List<Double> newCoordinates = generator.getNumbers();
        for (int i = 0; i < middlePoint.getNumberOfDimensions(); ++i) {
			newCoordinates.set(i, newCoordinates.get(i) * deviations.get(i) + middlePoint.getValue(i));
        }
        return new Point(newCoordinates);
    }
}

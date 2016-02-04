package pl.poznan.put.cs.idss.generator.generation;

import java.util.List;

public class AdditionalOutlierPointGenerator implements PointGenerator {

    private final Example middle;
    private static final int K_STANDARD_DEVIATION = 10;
    private final RandomGenerator generator;
    private final List<Double> deviations;

    public AdditionalOutlierPointGenerator(Example middle,
            NearestNeighbourSelector selector,
            NeighbourhoodStandardDeviationCalculator deviationCalculator,
            RandomGenerator generator,
            List<Example> examples) {
        this.middle = middle;
        this.generator = generator;
        deviations = getStandardDeviations(examples, selector, deviationCalculator);
    }

    private List<Double> getStandardDeviations(List<Example> examples,
            NearestNeighbourSelector selector,
            NeighbourhoodStandardDeviationCalculator deviationCalculator) {
        List<Example> nearest = selector.getNeighbours(K_STANDARD_DEVIATION, middle, examples);
        return deviationCalculator.calculateStandardDeviations(nearest);
    }

    public Point generate() {
        List<Double> newCoordinates = generator.getNumbers();
        for (int i = 0; i < middle.getPoint().getNumberOfDimensions(); ++i) {
			newCoordinates.set(i, newCoordinates.get(i) * deviations.get(i) + middle.getPoint().getValue(i));
        }
        return new Point(newCoordinates);
    }
}

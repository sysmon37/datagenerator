package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
import java.util.List;

public class NeighbourhoodStandardDeviationCalculator {

    public List<Double> calculateStandardDeviations(List<Example> examples) {
        checkExamplesSize(examples);
        final int dimensions = getNumberOfDimensions(examples);

        List<Double> deviations = new ArrayList<Double>();
        for (int i = 0; i < dimensions; ++i) {
            deviations.add(standardDeviationForDimension(examples, i));
        }
        return deviations;
    }

    private double standardDeviationForDimension(List<Example> examples, int dimension) {
        final double mean = calculateMean(examples, dimension);
        double variance = 0;

        for (Example instance : examples) {
            variance += Math.pow(instance.getPoint().getValue(dimension) - mean, 2);
        }

        variance /= examples.size() - 1;
        return Math.sqrt(variance);
    }

    private double calculateMean(List<Example> examples, int dimension) {
        double mean = 0;
        for (Example example : examples) {
            mean += example.getPoint().getValue(dimension);
        }
        mean /= examples.size();
        return mean;
    }

    private void checkExamplesSize(List<Example> examples) {
        if (examples.isEmpty()) {
            throw new IllegalArgumentException("Example list is empty!");
        }
    }

    private int getNumberOfDimensions(List<Example> examples) {
        final int dimensionality = examples.get(0).getPoint().getNumberOfDimensions();
        for (Example example : examples) {
            if (example.getPoint().getNumberOfDimensions() != dimensionality) {
                throw new IllegalArgumentException("Examples are located in different dimensions!");
            }
        }
        return dimensionality;
    }
}

package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.generation.AdditionalOutlierPointGenerator;
import pl.poznan.put.cs.idss.generator.generation.Example;
import pl.poznan.put.cs.idss.generator.generation.NearestNeighbourSelector;
import pl.poznan.put.cs.idss.generator.generation.NeighbourhoodStandardDeviationCalculator;

import java.util.List;

public class AdditionalPointGeneratorFactory {

    public AdditionalOutlierPointGenerator createOutlier(List<Example> examples,
            Example generatedExample) {
        return new AdditionalOutlierPointGenerator(generatedExample.getPoint(),
                new NearestNeighbourSelector(),
                new NeighbourhoodStandardDeviationCalculator(),
                RandomGeneratorFactory.createOutliersGenerator(generatedExample.getPoint().getNumberOfDimensions()),
                examples);
    }
}

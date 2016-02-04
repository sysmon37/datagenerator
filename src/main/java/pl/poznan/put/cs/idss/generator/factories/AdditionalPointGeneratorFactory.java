package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.generation.AdditionalOutlierPointGenerator;
import pl.poznan.put.cs.idss.generator.generation.Example;
import pl.poznan.put.cs.idss.generator.generation.ExampleDistanceCalculator;
import pl.poznan.put.cs.idss.generator.generation.NearestNeighbourSelector;
import pl.poznan.put.cs.idss.generator.generation.NeighbourhoodStandardDeviationCalculator;

import java.util.List;

public class AdditionalPointGeneratorFactory {

    public AdditionalOutlierPointGenerator createPointGenerator(List<Example> examples,
            Example generatedExample)
    {
        return new AdditionalOutlierPointGenerator(generatedExample,
                new NearestNeighbourSelector<Example>(new ExampleDistanceCalculator()),
                new NeighbourhoodStandardDeviationCalculator(),
                RandomGeneratorFactory.createOutliersGenerator(generatedExample.getPoint().getNumberOfDimensions()),
                examples);
    }
}

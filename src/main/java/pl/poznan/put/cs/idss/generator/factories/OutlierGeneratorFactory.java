package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.generation.ExampleDistanceCalculator;
import pl.poznan.put.cs.idss.generator.generation.IsInsideForbiddenZoneChecker;
import pl.poznan.put.cs.idss.generator.generation.NearestNeighbourSelector;
import pl.poznan.put.cs.idss.generator.generation.OutlierGenerator;
import pl.poznan.put.cs.idss.generator.generation.OutlierDistanceBreachedChecker;
import pl.poznan.put.cs.idss.generator.generation.OutlierFirstPointGenerator;
import pl.poznan.put.cs.idss.generator.generation.OutlierNeighbourhoodChecker;

import java.util.List;

class OutlierGeneratorFactory {

    public static OutlierGenerator createOutlierGenerator(double interOutlierDistance,
            List<OutlierDescription> outlierDescriptions,
            RegionGenerators allRegionGenerators) {
        return new OutlierGenerator(outlierDescriptions,
                new OutlierFirstPointGenerator(allRegionGenerators.getMinCoordinate(),
                        allRegionGenerators.getMaxCoordinate(),
                        RandomGeneratorFactory.createOverlappingExamplesGenerator(allRegionGenerators.getMinCoordinate().getNumDimensions())),
                new IsInsideForbiddenZoneChecker(allRegionGenerators.getRegionGenerators()),
                new OutlierDistanceBreachedChecker(interOutlierDistance),
                new OutlierNeighbourhoodChecker(new NearestNeighbourSelector<>(new ExampleDistanceCalculator())),
                new AdditionalPointGeneratorFactory());
    }
}

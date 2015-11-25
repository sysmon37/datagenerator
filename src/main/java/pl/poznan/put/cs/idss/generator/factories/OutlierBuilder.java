package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.generation.IsInsideForbiddenZoneChecker;
import pl.poznan.put.cs.idss.generator.generation.NearestNeighbourSelector;
import pl.poznan.put.cs.idss.generator.generation.OutlierGenerator;
import pl.poznan.put.cs.idss.generator.generation.OutlierDistanceBreachedChecker;
import pl.poznan.put.cs.idss.generator.generation.OutlierFirstPointGenerator;
import pl.poznan.put.cs.idss.generator.generation.OutlierNeighbourhoodChecker;

import java.util.List;

class OutlierBuilder {

    public static OutlierGenerator createOutlier(double interOutlierDistance,
            List<OutlierDescription> outlierDescriptions,
            RegionsDependencyCreator regionsDependencyCreator) {
        return new OutlierGenerator(outlierDescriptions,
                new OutlierFirstPointGenerator(regionsDependencyCreator.getMinCoordinate(),
                        regionsDependencyCreator.getMaxCoordinate(),
                        RandomGeneratorFactory.createOverlappingExamplesGenerator()),
                new IsInsideForbiddenZoneChecker(regionsDependencyCreator.getRegionGenerators()),
                new OutlierDistanceBreachedChecker(interOutlierDistance),
                new OutlierNeighbourhoodChecker(new NearestNeighbourSelector()),
                new AdditionalPointGeneratorFactory());
    }
}

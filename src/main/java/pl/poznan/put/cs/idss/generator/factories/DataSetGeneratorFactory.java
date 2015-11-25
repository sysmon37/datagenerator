package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.generation.DataSetGenerator;
import pl.poznan.put.cs.idss.generator.generation.OutlierGenerator;
import pl.poznan.put.cs.idss.generator.generation.RegionGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSetGeneratorFactory {

    public static DataSetGenerator create(double interOutlierDistance,
            List<RegionDescription> regionDescriptions,
            List<OutlierDescription> outlierDescriptions) {
        RegionsDependencyCreator regionsDependencyCreator = new RegionsDependencyCreator(regionDescriptions);
        OutlierGenerator outliersBuilder = OutlierBuilder.createOutlier(interOutlierDistance,
                outlierDescriptions,
                regionsDependencyCreator);
        return new DataSetGenerator(makeFlattened(regionsDependencyCreator.getRegionGenerators()), outliersBuilder);
    }

    private static List<RegionGenerator> makeFlattened(Map<Integer, List<RegionGenerator>> regions) {
        List<RegionGenerator> generators = new ArrayList<RegionGenerator>();
        for (List<RegionGenerator> generatorsForClass : regions.values()) {
            generators.addAll(generatorsForClass);
        }
        return generators;
    }
}

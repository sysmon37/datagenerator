package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.generation.DataSetGenerator;
import pl.poznan.put.cs.idss.generator.generation.OutlierGenerator;
import pl.poznan.put.cs.idss.generator.generation.RegionGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSetGeneratorFactory {

    public static DataSetGenerator createDataSetGenerator(double interOutlierDistance,
            List<RegionDescription> regionDescriptions,
            List<OutlierDescription> outlierDescriptions) {
        RegionGenerators allRegionGenerators = new RegionGenerators(regionDescriptions);
        OutlierGenerator outliersBuilder = OutlierGeneratorFactory.createOutlierGenerator(interOutlierDistance,
                outlierDescriptions,
                allRegionGenerators);
        return new DataSetGenerator(makeFlattened(allRegionGenerators.getRegionGenerators()), outliersBuilder);
    }

    private static List<RegionGenerator> makeFlattened(Map<Integer, List<RegionGenerator>> regions) {
        List<RegionGenerator> generators = new ArrayList<>();
        regions.values().stream().forEach((generatorsForClass) -> {
            generators.addAll(generatorsForClass);
        });
        return generators;
    }
}

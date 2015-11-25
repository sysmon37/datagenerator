package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.settings.Region;
import pl.poznan.put.cs.idss.generator.generation.HyperCircularDataShape;
import pl.poznan.put.cs.idss.generator.generation.HyperRectangularDataShape;
import pl.poznan.put.cs.idss.generator.generation.IntegumentalDataShape;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;
import pl.poznan.put.cs.idss.generator.generation.RegionGenerator;
import pl.poznan.put.cs.idss.generator.generation.Rotator;
import pl.poznan.put.cs.idss.generator.generation.DataShape;

import java.util.ArrayList;
import java.util.List;

public class RegionGeneratorBuilder {

    public static RegionGenerator createNonIntegumentalRegionGenerator(RegionDescription descr) {
        return createNonIntegumentalRegionGenerator(descr.getRegion(), descr.getClassIndex());
    }

    public static RegionGenerator createNonIntegumentalRegionGenerator(Region region, int classIndex) {
        RandomGenerator coreGenerator = RandomGeneratorFactory.createCoreExamplesGenerator(region);
        RandomGenerator overlappingGenerator = RandomGeneratorFactory.createOverlappingExamplesGenerator();
        DataShape shape = null;
        switch (region.getShape()) {
            case CIRCLE:
                shape = new HyperCircularDataShape(region,
                        coreGenerator,
                        overlappingGenerator);
                break;
            case RECTANGLE:
                shape = new HyperRectangularDataShape(region,
                        coreGenerator,
                        overlappingGenerator);
                break;
            default:
                throw new IllegalArgumentException("Wrong option for shape!");
        }
        return new RegionGenerator(shape,
                classIndex,
                createRotators(region));
    }

    public static RegionGenerator createIntegumentalRegionGenerator(RegionDescription descr, List<RegionGenerator> otherGenerators) {
        return createIntegumentalRegionGenerator(descr.getRegion(), descr.getClassIndex(), otherGenerators);
    }

    public static RegionGenerator createIntegumentalRegionGenerator(Region region, int classIndex, List<RegionGenerator> otherGenerators) {
        DataShape shape = new IntegumentalDataShape(region,
                RandomGeneratorFactory.createOverlappingExamplesGenerator(),
                otherGenerators);
        return new RegionGenerator(shape,
                classIndex,
                new ArrayList<>());
    }

    private static List<Rotator> createRotators(Region region) {
        ArrayList<Rotator> rotators = new ArrayList<>();
        for (pl.poznan.put.cs.idss.generator.settings.Rotation rotation : region.getRotations()) {
            rotators.add(new Rotator(region.getCenter(), rotation));
        }
        return rotators;
    }
}

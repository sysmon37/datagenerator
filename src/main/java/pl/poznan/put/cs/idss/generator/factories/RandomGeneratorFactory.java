package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.settings.Region;
import pl.poznan.put.cs.idss.generator.generation.GaussianDistributionGenerator;
import pl.poznan.put.cs.idss.generator.generation.LowDiscrepancySequenceGenerator;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;



public class RandomGeneratorFactory {

    
    public static RandomGenerator createCoreExamplesGenerator(Region region) {
        int numDimensions = region.getCenter().getNumDimensions();
        switch (region.getDistribution().getType()) {
            case UNIFORM:
                return createUniformGenerator(region);
            case NORMAL:
                return createNormalGenerator(region);
        }
        throw new IllegalArgumentException("Wrong option for distribution!");
    }

    public static RandomGenerator createOverlappingExamplesGenerator(int numDimensions) {
        return new LowDiscrepancySequenceGenerator(numDimensions);
    }

    public static RandomGenerator createOutliersGenerator(int numDimensions) {
        return new GaussianDistributionGenerator(numDimensions, 3.0);
    }
    
    protected static RandomGenerator createUniformGenerator(Region region) {
        int numDimensions = region.getCenter().getNumDimensions();
        return createUniformGenerator(numDimensions);
    }
    
    protected static RandomGenerator createUniformGenerator(int numDimensions) {
        return new LowDiscrepancySequenceGenerator(numDimensions);        
    }

    protected static RandomGenerator createNormalGenerator(Region region) {
        return new GaussianDistributionGenerator(region.getCenter().getNumDimensions(), region.getDistribution().getNumStandardDeviations());
    }
    
}

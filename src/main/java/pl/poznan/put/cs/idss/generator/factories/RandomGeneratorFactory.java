package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.generation.UniformDistributionGenerator;
import pl.poznan.put.cs.idss.generator.settings.Region;
import pl.poznan.put.cs.idss.generator.generation.GaussianDistributionGenerator;
import pl.poznan.put.cs.idss.generator.generation.LowDiscrepancySequenceGenerator;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;
import pl.poznan.put.cs.idss.generator.settings.ShapeType;


public class RandomGeneratorFactory {

    
    public static RandomGenerator createCoreExamplesGenerator(Region region) {
/*      For a circular region always use a normal (Gaussian) generator according to the new sampling technique.
        The applied algorithm maps intiial points into a uniform space.
*/
        if (region.getShape() == ShapeType.CIRCLE)
            return createNormalGenerator(region);
        else {
            switch (region.getDistribution().getType()) {
                case UNIFORM:
                    return createUniformGenerator(region);
                case NORMAL:
                    return createNormalGenerator(region);
            }
            throw new IllegalArgumentException("Wrong option for distribution!");
        }
    }

    public static RandomGenerator createOverlappingExamplesGenerator(int numDimensions) {
/*      We discard an LDS generator and change to to a uniform generator to allow for more than 40 dimensions.
        return new LowDiscrepancySequenceGenerator(numDimensions);*/
        return new UniformDistributionGenerator(numDimensions);
    }

    public static RandomGenerator createOutliersGenerator(int numDimensions) {
        /*  the number of standard deviations (2nd parameter below) changed 
            from 3.0 to 1.0 to better "distribute" R and O examples
        */
        double numStDev = 1.0;
        return new GaussianDistributionGenerator(numDimensions, numStDev);
    }
    
    protected static RandomGenerator createUniformGenerator(Region region) {
        int numDimensions = region.getCenter().getNumDimensions();
        return createUniformGenerator(numDimensions);
    }
    
    protected static RandomGenerator createUniformGenerator(int numDimensions) {
/*
        return new LowDiscrepancySequenceGenerator(numDimensions);
*/
        return new UniformDistributionGenerator(numDimensions);
    }

    protected static RandomGenerator createNormalGenerator(Region region) {
        return new GaussianDistributionGenerator(region.getCenter().getNumDimensions(), region.getDistribution().getNumStDevs());
    }
    
}

package pl.poznan.put.cs.idss.generator.generation;

import lombok.Getter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;

@Getter
public class GaussianDistributionGenerator extends RandomGenerator {

    private final double _numStandardDeviations;

    public GaussianDistributionGenerator(int numDimensions, double numStandardDeviations) {
        this(numDimensions, numStandardDeviations, new MersenneTwister(RANDOM_SEED));
    }
    
    public GaussianDistributionGenerator(int numDimensions, double numStandardDeviations, BitsStreamGenerator numberGenerator) {
        super(numDimensions, numberGenerator);
        Validate.validState(numStandardDeviations > 0);        
        _numStandardDeviations = numStandardDeviations;
    }
        
    @Override
    protected double getNumber(double mean, double range) {
        double value = getNumberGenerator().nextGaussian();
        return value * range / getNumStandardDeviations() + mean;
    }
}

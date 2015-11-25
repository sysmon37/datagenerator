package pl.poznan.put.cs.idss.generator.generation;

import java.util.Random;
import org.apache.commons.lang3.Validate;


public class GaussianDistributionGenerator extends RandomGenerator
{
	private double standardDeviationCoefficient;

	public GaussianDistributionGenerator(Random generationAlgorithm, double numStandardDeviations)
	{
            super(generationAlgorithm);
            Validate.validState(numStandardDeviations > 0);
            standardDeviationCoefficient = numStandardDeviations;
	}

	public double getNumber(double mean, double range)
	{
		double value = generationAlgorithm.nextGaussian();
		return value * range/standardDeviationCoefficient + mean;
	}	
}
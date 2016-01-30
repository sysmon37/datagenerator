package pl.poznan.put.cs.idss.generator.generation;

import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;


public class UniformDistributionGenerator extends RandomGenerator
{
	public UniformDistributionGenerator(int numDimensions)
	{
		this(numDimensions, new MersenneTwister());
	}

	public UniformDistributionGenerator(int numDimensions, BitsStreamGenerator numberGenerator)
	{
		super(numDimensions, numberGenerator);
	}
        
        @Override
	protected double getNumber(double mean, double range)
	{
		double lowerBound = mean - range;
		double upperBound = mean + range;
		double value = getNumberGenerator().nextDouble();
		return value * (upperBound - lowerBound) + lowerBound;
	}	
}
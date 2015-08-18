package generator;

import java.util.Random;


public class UniformDistributionGenerator extends RandomGenerator
{
	public UniformDistributionGenerator(Random generationAlgorithm)
	{
		super(generationAlgorithm);
	}

	public double getNumber(double mean, double range)
	{
		double lowerBound = mean - range;
		double upperBound = mean + range;
		double value = generationAlgorithm.nextDouble();
		return value * (upperBound - lowerBound) + lowerBound;
	}	
}
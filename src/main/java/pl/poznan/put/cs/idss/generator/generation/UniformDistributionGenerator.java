package pl.poznan.put.cs.idss.generator.generation;

import java.util.Random;


public class UniformDistributionGenerator extends RandomGenerator
{
	public UniformDistributionGenerator(Random generationAlgorithm, int dimensionality)
	{
		super(generationAlgorithm, dimensionality);
	}

	public double getNumber(double mean, double range)
	{
		double lowerBound = mean - range;
		double upperBound = mean + range;
		double value = generationAlgorithm.nextDouble();
		return value * (upperBound - lowerBound) + lowerBound;
	}	
}
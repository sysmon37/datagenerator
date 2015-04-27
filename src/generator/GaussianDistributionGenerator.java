package generator;

import java.util.Random;


public class GaussianDistributionGenerator extends RandomGenerator
{
	private double standardDeviationCoefficient;

	public GaussianDistributionGenerator(Random generationAlgorithm, String standardDeviationCoefficient)
	{
		super(generationAlgorithm);
		if(standardDeviationCoefficient.length() == 0)
			standardDeviationCoefficient = "3";
		this.standardDeviationCoefficient = Double.parseDouble(standardDeviationCoefficient);
	}

	public double getNumber(double mean, double range)
	{
		double value = generationAlgorithm.nextGaussian();
		return value * range/standardDeviationCoefficient + mean;
	}	
}
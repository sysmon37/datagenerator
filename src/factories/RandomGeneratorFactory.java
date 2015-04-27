package factories;

import generator.GaussianDistributionGenerator;
import generator.RandomGenerator;
import generator.UniformDistributionGenerator;

import java.util.Random;


public class RandomGeneratorFactory
{
	public final static long RANDOM_SEED = System.nanoTime();
	private static final Random random = new Random(RANDOM_SEED);
	
	public static RandomGenerator makeCoreExamplesGenerator(RegionDescription c)
	{
		switch(c.distribution.charAt(0))
		{
			case 'U':
				return new UniformDistributionGenerator(random);
			case 'N':
				return new GaussianDistributionGenerator(random, c.distribution.substring(1));
		}
		throw new IllegalArgumentException("Wrong option for distribution!");
	}

	public static RandomGenerator makeOverlappingExamplesGenerator()
	{
		return new UniformDistributionGenerator(random);
	}
	
	public static RandomGenerator makeOutliersGenerator()
	{
		return new GaussianDistributionGenerator(random, "3");
	}
}
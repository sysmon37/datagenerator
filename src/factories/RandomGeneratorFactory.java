package factories;

import generator.GaussianDistributionGenerator;
import generator.HighQualityRandom;
import generator.RandomGenerator;
import generator.UniformDistributionGenerator;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;


public class RandomGeneratorFactory
{
	public final static long RANDOM_SEED = generateRandomSeed();
	private static final Random random = new HighQualityRandom(RANDOM_SEED);//new SecureRandom();//new Random(RANDOM_SEED);
	
	private static long generateRandomSeed()
	{
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.put(SecureRandom.getSeed(Long.BYTES));
	    buffer.flip();
	    return buffer.getLong();
	}
	
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
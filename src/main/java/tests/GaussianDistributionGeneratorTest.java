package tests;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import static org.mockito.Mockito.*;

import DataSetGenerator.GaussianDistributionGenerator;
import DataSetGenerator.RandomGenerator;



public class GaussianDistributionGeneratorTest
{
	private static final double mean = -5.8;
	private static final double spread = 15;
	private static final double numberOfStdContainedInSpread = 4;
	private static final double pseudoGeneratedNumber = 0.44;
	private Random generationAlgorithmMock = mock(Random.class);
	private RandomGenerator generator = new GaussianDistributionGenerator(generationAlgorithmMock, "" + numberOfStdContainedInSpread);
	
	@Test
	public void whenCalledGetNumber_returnsProperValue()
	{
		when(generationAlgorithmMock.nextGaussian()).thenReturn(pseudoGeneratedNumber);
		
		assertEquals(mean + pseudoGeneratedNumber * spread/numberOfStdContainedInSpread,
				     generator.getNumber(mean, spread),
				     0.00000001);
		verify(generationAlgorithmMock, times(1)).nextGaussian();
	}
}


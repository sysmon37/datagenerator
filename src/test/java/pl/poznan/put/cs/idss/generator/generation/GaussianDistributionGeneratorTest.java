package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;
import pl.poznan.put.cs.idss.generator.generation.GaussianDistributionGenerator;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import static org.mockito.Mockito.*;



public class GaussianDistributionGeneratorTest
{
	private static final double numberOfStdContainedInSpread = 4;
	private static final double pseudoGeneratedNumber = 0.44;
	private Random generationAlgorithmMock = mock(Random.class);
	private RandomGenerator generator = new GaussianDistributionGenerator(generationAlgorithmMock, 1, numberOfStdContainedInSpread);
	
	@Test
	public void whenCalledGetNumber_returnsProperValue()
	{
		when(generationAlgorithmMock.nextGaussian()).thenReturn(pseudoGeneratedNumber);
		
		assertEquals(Arrays.asList(pseudoGeneratedNumber/numberOfStdContainedInSpread),
				     generator.getNumbers());
		verify(generationAlgorithmMock, times(1)).nextGaussian();
	}
}


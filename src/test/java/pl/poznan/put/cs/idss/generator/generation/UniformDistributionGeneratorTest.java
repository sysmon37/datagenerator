package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;
import pl.poznan.put.cs.idss.generator.generation.UniformDistributionGenerator;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class UniformDistributionGeneratorTest
{
	private static final int intervalLength = 2;
	private static final int lowerBound = -1;
	private static final double pseudoGeneratedNumber = 0.44;
	private Random generationAlgorithmMock = mock(Random.class);
	private RandomGenerator generator = new UniformDistributionGenerator(generationAlgorithmMock, 1);
	
	@Test
	public void whenCalledGetNumber_returnsProperValue()
	{
		when(generationAlgorithmMock.nextDouble()).thenReturn(pseudoGeneratedNumber);
		
		assertEquals(Arrays.asList(lowerBound + pseudoGeneratedNumber * intervalLength),
				     generator.getNumbers());
		verify(generationAlgorithmMock, times(1)).nextDouble();
	}
}


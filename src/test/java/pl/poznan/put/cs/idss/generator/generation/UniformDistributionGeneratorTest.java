package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;
import pl.poznan.put.cs.idss.generator.generation.UniformDistributionGenerator;

import java.util.Random;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class UniformDistributionGeneratorTest
{
	private static final int upperBound = 5;
	private static final int intervalLength = 3;
	private static final int lowerBound = 2;
	private static final double spread = (upperBound - lowerBound)/2.0;
	private static final double pseudoGeneratedNumber = 0.44;
	private Random generationAlgorithmMock = mock(Random.class);
	private RandomGenerator generator = new UniformDistributionGenerator(generationAlgorithmMock, 1);
	
	@Test
	public void whenCalledGetNumber_returnsProperValue()
	{
		when(generationAlgorithmMock.nextDouble()).thenReturn(pseudoGeneratedNumber);
		
		assertEquals(lowerBound + pseudoGeneratedNumber * intervalLength,
				     generator.getNumber(lowerBound + spread, spread),
				     0.00000001);
		verify(generationAlgorithmMock, times(1)).nextDouble();
	}
}


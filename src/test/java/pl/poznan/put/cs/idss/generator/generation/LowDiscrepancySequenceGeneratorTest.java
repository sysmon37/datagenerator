package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.random.RandomVectorGenerator;
import org.junit.Test;


public class LowDiscrepancySequenceGeneratorTest
{
	private static final int intervalLength = 2;
	private static final int lowerBound = -1;
	private static final double pseudoGeneratedNumber = 0.44;
	private Random generationAlgorithmMock = mock(Random.class);
	private RandomVectorGenerator randomVectorGenerator = mock(RandomVectorGenerator.class);
	private RandomGenerator generator = new LowDiscrepancySequenceGenerator(generationAlgorithmMock, 1, randomVectorGenerator);
	
	@Test
	public void whenCalledGetNumber_returnsProperValue()
	{
		double[] randomVector = {pseudoGeneratedNumber};
		when(randomVectorGenerator.nextVector()).thenReturn(randomVector);
		assertEquals(Arrays.asList(lowerBound + pseudoGeneratedNumber * intervalLength),
					 generator.getNumbers());
	}
}
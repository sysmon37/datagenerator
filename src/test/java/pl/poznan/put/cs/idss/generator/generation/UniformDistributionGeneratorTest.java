package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.apache.commons.math3.random.BitsStreamGenerator;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class UniformDistributionGeneratorTest
{
	private static final int INTERVAL_LENGTH = 2;
	private static final int LOWER_BOUND = -1;
	private static final double PSEUDO_GENERATED_NUMBER = 0.44;
	private final BitsStreamGenerator generationAlgorithmMock = mock(BitsStreamGenerator.class);
	private final RandomGenerator generator = new UniformDistributionGenerator(1, generationAlgorithmMock);
	
	@Test
	public void whenCalledGetNumber_returnsProperValue()
	{
		when(generationAlgorithmMock.nextDouble()).thenReturn(PSEUDO_GENERATED_NUMBER);
		
		assertEquals(Arrays.asList(LOWER_BOUND + PSEUDO_GENERATED_NUMBER * INTERVAL_LENGTH),
				     generator.getNumbers());
		verify(generationAlgorithmMock, times(1)).nextDouble();
	}
}


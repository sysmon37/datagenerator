package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.apache.commons.math3.random.BitsStreamGenerator;

import org.junit.Test;

import static org.mockito.Mockito.*;



public class GaussianDistributionGeneratorTest
{
	private static final double NUM_STANDARD_DEVIATIONS = 4;
	private static final double RANDOM_NUMBER = 0.44;
	private final BitsStreamGenerator numberGeneratorMock = mock(BitsStreamGenerator.class);
	private final RandomGenerator generator = new GaussianDistributionGenerator(1, NUM_STANDARD_DEVIATIONS, numberGeneratorMock);
	
	@Test
	public void whenCalledGetNumber_returnsProperValue()
	{
		when(numberGeneratorMock.nextGaussian()).thenReturn(RANDOM_NUMBER);
		
		assertEquals(Arrays.asList(RANDOM_NUMBER/NUM_STANDARD_DEVIATIONS),
				     generator.getNumbers());
		verify(numberGeneratorMock, times(1)).nextGaussian();
	}
}


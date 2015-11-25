package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;
import pl.poznan.put.cs.idss.generator.generation.UniformDistributionGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import static org.mockito.Mockito.*;



public class RandomGeneratorTest
{
	private Random generationAlgorithmMock = mock(Random.class);
	private RandomGenerator generator = new UniformDistributionGenerator(generationAlgorithmMock);
	
	@Test
	public void whenShuffleIsCalled_returnsShuffledList()
	{
		when(generationAlgorithmMock.nextInt(5)).thenReturn(0);
		when(generationAlgorithmMock.nextInt(4)).thenReturn(2);
		when(generationAlgorithmMock.nextInt(3)).thenReturn(1);
		when(generationAlgorithmMock.nextInt(2)).thenReturn(0);

		List<Double> values = Arrays.asList(5., 3., 4., 1., 2.);
		generator.shuffle(values);
		for(int i = 0 ; i < values.size() ; ++i)
			assertEquals(i + 1, values.get(i), 0);
		
		for(int i = 2 ; i <= values.size(); ++i)
			verify(generationAlgorithmMock, times(1)).nextInt(i);
	}
}


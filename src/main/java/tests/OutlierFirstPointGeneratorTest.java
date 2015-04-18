package tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import DataSetGenerator.OutlierFirstPointGenerator;
import DataSetGenerator.Point;
import DataSetGenerator.RandomGenerator;


public class OutlierFirstPointGeneratorTest
{
	private List<Double> minimumRanges = Arrays.asList(-8., 8.2);
	private List<Double> maximumRanges = Arrays.asList(0.4, 90.2);
	private RandomGenerator numberGenerator = mock(RandomGenerator.class);
	private OutlierFirstPointGenerator pointGenerator = new OutlierFirstPointGenerator(minimumRanges,
																					 maximumRanges,
																					 numberGenerator);
	
	@Test
	public void whenGenerateIsCalled_returnsPoint()
	{
		when(numberGenerator.getNumber(-3.8, 4.20)).thenReturn(4.05);
		when(numberGenerator.getNumber(49.2, 41.0)).thenReturn(-3.4);
		assertEquals(new Point(Arrays.asList(4.05, -3.4)), pointGenerator.generate());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenMinimumExceedsMaximum_throws()
	{
		new OutlierFirstPointGenerator(Arrays.asList(0.4), Arrays.asList(0.2), null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenMinimumValuesHaveDifferenteSizeThanMaximumValues_throws()
	{
		new OutlierFirstPointGenerator(Arrays.asList(0.1, 0.15), Arrays.asList(0.2), null);
	}
}
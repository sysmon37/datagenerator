package tests;

import static org.junit.Assert.assertEquals;
import generator.Instance;
import generator.NeighbourhoodStandardDeviationCalculator;
import generator.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class NeighbourhoodStandardDeviationCalculatorTest
{

	private NeighbourhoodStandardDeviationCalculator calculator = new NeighbourhoodStandardDeviationCalculator();
	private List<Instance> instances;
	
	public NeighbourhoodStandardDeviationCalculatorTest()
	{
		instances = new ArrayList<Instance>(); 
		
		for(int i = 1 ; i <= 10 ; ++i)
			instances.add(new Instance(new Point(Arrays.asList(i * 11.1, i * 223.)), null));
	}

	@Test
	public void whenInstancesAreProvided_standardDeviationsAreCalculated()
	{
		List<Double> deviations = calculator.calculateStandardDeviations(instances);
		assertEquals(deviations.size(), instances.get(0).getPoint().getNumberOfDimensions());
		assertEquals(33.60691893, deviations.get(0), 0.00001);
		assertEquals(675.166029, deviations.get(1), 0.00001);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenPointsHaveDifferentDimensionalities_throws()
	{
		instances.add(new Instance(new Point(Arrays.asList(0., 1., 2.)), null));
		calculator.calculateStandardDeviations(instances);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenInstancesListIsEmpty_throws()
	{
		calculator.calculateStandardDeviations(new ArrayList<Instance>());
	}
}
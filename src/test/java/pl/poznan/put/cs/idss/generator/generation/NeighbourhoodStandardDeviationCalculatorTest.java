package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class NeighbourhoodStandardDeviationCalculatorTest
{

	private NeighbourhoodStandardDeviationCalculator calculator = new NeighbourhoodStandardDeviationCalculator();
	private List<Example> examples;
	
	public NeighbourhoodStandardDeviationCalculatorTest()
	{
		examples = new ArrayList<Example>(); 
		
		for(int i = 1 ; i <= 10 ; ++i)
			examples.add(new Example(new Point(Arrays.asList(i * 11.1, i * 223.)), 0));
	}

	@Test
	public void whenExamplesAreProvided_standardDeviationsAreCalculated()
	{
		List<Double> deviations = calculator.calculateStandardDeviations(examples);
		assertEquals(deviations.size(), examples.get(0).getPoint().getNumberOfDimensions());
		assertEquals(33.60691893, deviations.get(0), 0.00001);
		assertEquals(675.166029, deviations.get(1), 0.00001);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenPointsHaveDifferentDimensionalities_throws()
	{
		examples.add(new Example(new Point(Arrays.asList(0., 1., 2.)), 0));
		calculator.calculateStandardDeviations(examples);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenExampleListIsEmpty_throws()
	{
		calculator.calculateStandardDeviations(new ArrayList<Example>());
	}
}
package tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import generator.AdditionalOutlierPointGenerator;
import generator.Instance;
import generator.NearestNeighbourSelector;
import generator.NeighbourhoodStandardDeviationCalculator;
import generator.Point;
import generator.RandomGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AdditionalOutlierPointGeneratorTest
{
	private final Point middlePoint = new Point(Arrays.asList(200., -100.));
	private final Point generatedPoint = new Point(Arrays.asList(201., -102.));
	private List<Double> deviations = Arrays.asList(3.5, 2.8);
	private NearestNeighbourSelector selector = mock(NearestNeighbourSelector.class);
	private NeighbourhoodStandardDeviationCalculator calculator = mock(NeighbourhoodStandardDeviationCalculator.class);
	private RandomGenerator numberGenerator = mock(RandomGenerator.class);
	private List<Instance> instances = new ArrayList<Instance>();
	private AdditionalOutlierPointGenerator pointGenerator;
	
	public AdditionalOutlierPointGeneratorTest()
	{
		for(int i = 0 ; i < 100 ; ++i)
			instances.add(new Instance(new Point(Arrays.asList((double)i, 100.-i)), 1));
	}
	
	@Test
	public void whenGenerateIsCalled_returnsPointBasedOnLocalNeighbourhoodOfTheMiddlePoint()
	{
		when(selector.getNeighbours(10, middlePoint, instances)).thenReturn(instances.subList(91, 100));
		when(calculator.calculateStandardDeviations(instances.subList(91, 100))).thenReturn(deviations);
		when(numberGenerator.getNumber(middlePoint.getValue(0), deviations.get(0))).thenReturn(generatedPoint.getValue(0));
		when(numberGenerator.getNumber(middlePoint.getValue(1), deviations.get(1))).thenReturn(generatedPoint.getValue(1));
		pointGenerator = new AdditionalOutlierPointGenerator(middlePoint, selector, calculator, numberGenerator, instances);
		assertEquals(generatedPoint, pointGenerator.generate());
	}
}
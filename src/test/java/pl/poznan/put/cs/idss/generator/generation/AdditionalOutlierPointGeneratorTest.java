package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AdditionalOutlierPointGeneratorTest
{
	private final Example middle = new Example(new Point(Arrays.asList(200., -100.)), 0);
	private final Point generatedPoint = new Point(Arrays.asList(201., -102.));
	private List<Double> deviations = Arrays.asList(3.5, 2.8);
	private NearestNeighbourSelector selector = mock(NearestNeighbourSelector.class);
	private NeighbourhoodStandardDeviationCalculator calculator = mock(NeighbourhoodStandardDeviationCalculator.class);
	private RandomGenerator numberGenerator = mock(RandomGenerator.class);
	private List<Example> examples = new ArrayList<>();
	private AdditionalOutlierPointGenerator pointGenerator;
	
	public AdditionalOutlierPointGeneratorTest()
	{
		for(int i = 0 ; i < 100 ; ++i)
			examples.add(new Example(new Point(Arrays.asList((double)i, 100.-i)), 1));
	}
	
	@Test
	public void whenGenerateIsCalled_returnsPointBasedOnLocalNeighbourhoodOfTheMiddlePoint()
	{
		when(selector.getNeighbours(10, middle, examples)).thenReturn(examples.subList(91, 100));
		when(calculator.calculateStandardDeviations(examples.subList(91, 100))).thenReturn(deviations);
		when(numberGenerator.getNumbers()).thenReturn(Arrays.asList((generatedPoint.getValue(0) - middle.getPoint().getValue(0))/deviations.get(0),
														 (generatedPoint.getValue(1) - middle.getPoint().getValue(1))/deviations.get(1)));
		pointGenerator = new AdditionalOutlierPointGenerator(middle, selector, calculator, numberGenerator, examples);
		assertEquals(generatedPoint, pointGenerator.generate());
	}
}
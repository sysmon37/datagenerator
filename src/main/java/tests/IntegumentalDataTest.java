package tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import DataSetGenerator.IntegumentalData;
import DataSetGenerator.Point;
import DataSetGenerator.RandomGenerator;
import DataSetGenerator.Region;
import DataSetGenerator.Shape;

public class IntegumentalDataTest
{
	private List<Double> middleCoordinates = Arrays.asList(11.0, 170.5);
	private List<Double> axisLengths = Arrays.asList(6.0, 55.5);
	private RandomGenerator generator = mock(RandomGenerator.class);
	private List<Region> figures = Arrays.asList(mock(Region.class), mock(Region.class));
	private Shape shape = new IntegumentalData(middleCoordinates, axisLengths, generator, figures);
	private List<Double> generatedCoordinates = Arrays.asList(-5.4, 33.1);
	
	@Test
	public void whenGenerateCorePointIsCalled_returnsCorePoint()
	{
		when(generator.getNumber(middleCoordinates.get(0), axisLengths.get(0))).
			thenReturn(generatedCoordinates.get(0));
		when(generator.getNumber(middleCoordinates.get(1), axisLengths.get(1))).
			thenReturn(generatedCoordinates.get(1));
	
		assertEquals(new Point(generatedCoordinates), shape.generateCorePoint());
	}
	
	@Test
	public void whenGeneratedCorePointIsInsideOtherShape_returnsAnotherCorePoint()
	{		
		List<Double> skippedCoordinates = Arrays.asList(-51.4, 27.1);
		
		when(generator.getNumber(middleCoordinates.get(0), axisLengths.get(0))).
			thenReturn(skippedCoordinates.get(0), generatedCoordinates.get(0));
		when(generator.getNumber(middleCoordinates.get(1), axisLengths.get(1))).
			thenReturn(skippedCoordinates.get(1), generatedCoordinates.get(1));
		when(figures.get(0).isCovered(new Point(skippedCoordinates))).
			thenReturn(true);
		
		assertEquals(new Point(generatedCoordinates), shape.generateCorePoint());
	}
	
	@Test
	public void whenGeneratesOverlappingPoint_returnsOverlappingPoint()
	{		
		assertThatGeneratesOverlappingPointFromFirstFigure();
	}
	
	@Test
	public void whenGeneratesMoreOverlappingPoints_returnsThemSequentiallyFromFigures()
	{		
		assertThatGeneratesOverlappingPointFromFirstFigure();
		
		List<Double> anotherCoordinates = Arrays.asList(-51.4, 27.1);
		when(figures.get(1).generateOverlappingPoint()).thenReturn(new Point(anotherCoordinates));
		assertEquals(new Point(anotherCoordinates), shape.generateOverlappingPoint());
		
		assertThatGeneratesOverlappingPointFromFirstFigure();
	}

	private void assertThatGeneratesOverlappingPointFromFirstFigure()
	{
		when(figures.get(0).generateOverlappingPoint()).thenReturn(new Point(generatedCoordinates));
		assertEquals(new Point(generatedCoordinates), shape.generateOverlappingPoint());
	}
}
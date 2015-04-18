package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import DataSetGenerator.Point;
import DataSetGenerator.RandomGenerator;
import DataSetGenerator.HyperRectangularData;
import DataSetGenerator.Shape;

public class HyperRectangularDataTest
{
	private static final int borderSize = 4;
	private List<Double> middleCoordinates = Arrays.asList(11.0, 170.5);
	private List<Double> axisLengths = Arrays.asList(6.0, 55.5);
	private RandomGenerator generator = mock(RandomGenerator.class);
	private List<Double> overlappingPointCoordinates = Arrays.asList(20.3, 143.14);
	private List<Double> corePointCoordinates = Arrays.asList(6.5, 140.1);
	private double outlierForbiddenZone = 20.;
	
	@Test
	public void whenGenerateCorePointIsCalled_returnsCorePoint()
	{		
		when(generator.getNumber(middleCoordinates.get(0), axisLengths.get(0))).
			thenReturn(corePointCoordinates.get(0));
		when(generator.getNumber(middleCoordinates.get(1), axisLengths.get(1))).
			thenReturn(corePointCoordinates.get(1));
		
		Shape shape = new HyperRectangularData(middleCoordinates,
											   axisLengths,
											   borderSize,
											   outlierForbiddenZone,
											   generator,
											   null);
		
		assertEquals(new Point(corePointCoordinates), shape.generateCorePoint());
	}
	
	@Test
	public void whenGenerateOverlappingPointIsCalled_returnsOverlappingPoint()
	{		
		when(generator.getNumber(middleCoordinates.get(0), axisLengths.get(0) + borderSize)).
			thenReturn(overlappingPointCoordinates.get(0));
		when(generator.getNumber(middleCoordinates.get(1), axisLengths.get(1) + borderSize)).
			thenReturn(overlappingPointCoordinates.get(1));
		
		Shape shape = new HyperRectangularData(middleCoordinates,
											   axisLengths,
											   borderSize,
											   outlierForbiddenZone,
											   null,
											   generator);
		
		assertEquals(new Point(overlappingPointCoordinates), shape.generateOverlappingPoint());
	}
	
	@Test
	public void whenGeneratedOverlappingPointIsInsideCore_returnsAnotherOneOverlappingPoint()
	{		
		when(generator.getNumber(middleCoordinates.get(0), axisLengths.get(0) + borderSize)).
			thenReturn(corePointCoordinates.get(0), overlappingPointCoordinates.get(0));
		when(generator.getNumber(middleCoordinates.get(1), axisLengths.get(1) + borderSize)).
			thenReturn(corePointCoordinates.get(1), overlappingPointCoordinates.get(1));
		
		Shape shape = new HyperRectangularData(middleCoordinates,
											   axisLengths,
											   borderSize,
											   outlierForbiddenZone,
											   null,
											   generator);
		
		assertEquals(new Point(overlappingPointCoordinates), shape.generateOverlappingPoint());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenMiddlePointDimensionsDoesNotEqualsToAxisLengths_throws()
	{
		new HyperRectangularData(middleCoordinates, Arrays.asList(0.5), 0, 0, null, null);
	}
		
	@Test(expected = IllegalArgumentException.class)
	public void whenMarginIsNegative_throws()
	{
		new HyperRectangularData(middleCoordinates, axisLengths, -1, 0, null, null);
	}
	
	@Test
	public void whenIsCoverdCalled_returnsProperValue()
	{		
		Shape shape = new HyperRectangularData(middleCoordinates,
											   axisLengths,
											   borderSize,
											   outlierForbiddenZone,
											   null,
											   null);
		
		assertTrue(shape.isCovered(new Point(corePointCoordinates)));
		assertTrue(shape.isCovered(new Point(overlappingPointCoordinates)));
		
		assertFalse(shape.isCovered(new Point(Arrays.asList(1.0, 110.0))));
		assertFalse(shape.isCovered(new Point(Arrays.asList(0.5, 111.0))));
		assertFalse(shape.isCovered(new Point(Arrays.asList(21.2, 226.0))));
		assertFalse(shape.isCovered(new Point(Arrays.asList(17.0, 230.1))));
	}
	
	@Test
	public void whenIsInOutlierForbiddenZone_returnsCorrectValue()
	{
		Shape shape = new HyperRectangularData(middleCoordinates,
											   axisLengths,
											   borderSize,
											   outlierForbiddenZone,
											   null,
											   null);
		assertTrue(shape.isInOutlierForbiddenZone(new Point(corePointCoordinates)));
		assertFalse(shape.isInOutlierForbiddenZone(new Point(Arrays.asList(41.0, 250.5))));
		assertTrue(shape.isInOutlierForbiddenZone(new Point(overlappingPointCoordinates)));
		assertTrue(shape.isInOutlierForbiddenZone(new Point(Arrays.asList(40.0, 245.))));
		assertFalse(shape.isInOutlierForbiddenZone(new Point(Arrays.asList(42.0, 245.5))));
	}
}


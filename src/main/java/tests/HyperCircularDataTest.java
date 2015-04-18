package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import org.junit.Test;

import DataSetGenerator.HyperCircularData;
import DataSetGenerator.Point;
import DataSetGenerator.RandomGenerator;
import DataSetGenerator.Shape;


public class HyperCircularDataTest
{
	private RandomGenerator coreExamplesGenerator = mock(RandomGenerator.class);
	private RandomGenerator overlappingExamplesGenerator = mock(RandomGenerator.class);

	@Test(expected = IllegalArgumentException.class)
	public void whenNegativeBorderSize_throws()
	{
		new HyperCircularData(Arrays.asList(0.),
							 Arrays.asList(1.),
							 -1,
							 6,
							 coreExamplesGenerator,
							 overlappingExamplesGenerator);
	}
	
	@Test
	public void whenOneDimensionalDataAndCoreIsNormalized_returnsCorePoint()
	{
		Shape shape = new HyperCircularData(Arrays.asList(0.),
											 Arrays.asList(1.),
											 0,
											 6,
											 coreExamplesGenerator,
											 overlappingExamplesGenerator);
		when(coreExamplesGenerator.getNumber(0, 1)).thenReturn(-0.39);
		assertEquals(new Point(Arrays.asList(-0.39)), shape.generateCorePoint());
	}

	@Test
	public void whenOneDimensionalDataAndCoreIsShifted_returnsCorePoint()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2.),
											 Arrays.asList(1.),
											 0,
											 6,
											 coreExamplesGenerator,
											 overlappingExamplesGenerator);
		when(coreExamplesGenerator.getNumber(-2, 1)).thenReturn(-0.39 -2.);
		assertEquals(new Point(Arrays.asList(-2.39)), shape.generateCorePoint());
	}
	
	@Test
	public void whenOneDimensionalDataAndCoreIsShiftedAndStretched_returnsCorePoint()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2.),
											 Arrays.asList(5.),
											 0,
											 6,
											 coreExamplesGenerator,
											 overlappingExamplesGenerator);
		when(coreExamplesGenerator.getNumber(-2, 5)).thenReturn(-0.39 * 5 -2);
		assertEquals(new Point(Arrays.asList(-3.95)), shape.generateCorePoint());
	}
		
	@Test
	public void whenCoreIsTwoDimensional_returnsCorePoint()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2., 25.),
											 Arrays.asList(5., 10.),
											 0,
											 6,
											 coreExamplesGenerator,
											 overlappingExamplesGenerator);
		when(coreExamplesGenerator.getNumber(-2, 5.)).thenReturn(-0.4 * 5 -2);
		when(coreExamplesGenerator.getNumber(25, 10.)).thenReturn(-0.3 * 10 +25);
		when(overlappingExamplesGenerator.getNumber(0.25, 0.25)).thenReturn(0.4);
		assertEquals(new Point(Arrays.asList(-4., 22.0)), shape.generateCorePoint());
	}
	
	@Test
	public void whenGeneratedCorePointIsNotInCore_returnsAnotherCorePoint()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2., 25.),
											 Arrays.asList(5., 10.),
											 0,
											 6,
											 coreExamplesGenerator,
											 overlappingExamplesGenerator);
		when(coreExamplesGenerator.getNumber(-2, 5.)).thenReturn(-7., -4.);
		when(coreExamplesGenerator.getNumber(25, 10.)).thenReturn(26., 22.);
		assertEquals(new Point(Arrays.asList(-4., 22.0)), shape.generateCorePoint());
	}
	
	@Test
	public void whenOneDimensionalData_isCoveredReturnsTrueIfPointIsInCore()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2.),
											 Arrays.asList(0.5),
											 0,
											 6,
											 coreExamplesGenerator,
											 overlappingExamplesGenerator);
		assertTrue(shape.isCovered(new Point(Arrays.asList(-1.7))));
		assertFalse(shape.isCovered(new Point(Arrays.asList(0.3))));
		assertFalse(shape.isCovered(new Point(Arrays.asList(-3.0))));
	}
	
	@Test
	public void whenTwoDimensionalData_isCoveredReturnsTrueIfPointIsInCore()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2.,25.),
										   Arrays.asList(5., 10.),
										   0,
										   6,
										   coreExamplesGenerator,
										   overlappingExamplesGenerator);
		assertTrue(shape.isCovered(new Point(Arrays.asList(-2.,35.))));
		assertTrue(shape.isCovered(new Point(Arrays.asList(-7.,25.))));
		assertFalse(shape.isCovered(new Point(Arrays.asList(-7.,26.))));
		assertFalse(shape.isCovered(new Point(Arrays.asList(-6., 32.))));
		assertTrue(shape.isCovered(new Point(Arrays.asList(-6., 30.))));
	}
	
	@Test
	public void whenBorderSizeIsNotEqualZero_isCoveredReturnsTrueIfPointIsInCoreOrOverlapping()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2.,25.),
				   Arrays.asList(5., 10.),
				   3,
				   6,
				   coreExamplesGenerator,
				   overlappingExamplesGenerator);
		assertTrue(shape.isCovered(new Point(Arrays.asList(-2.,38.))));
		assertTrue(shape.isCovered(new Point(Arrays.asList(-10.,25.))));
		assertFalse(shape.isCovered(new Point(Arrays.asList(-10.,26.))));
		assertFalse(shape.isCovered(new Point(Arrays.asList(-7., 36.))));
		assertTrue(shape.isCovered(new Point(Arrays.asList(-7., 35.))));
	}
	
	@Test
	public void whenOneDimensionalData_generateOverlappingPointReturnsAppropriatePoint()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2.),
				   Arrays.asList(5.),
				   3,
				   6,
				   coreExamplesGenerator,
				   overlappingExamplesGenerator);
	
		when(overlappingExamplesGenerator.getNumber(-2., 8.)).thenReturn(4.);
		assertEquals(new Point(Arrays.asList(4.)), shape.generateOverlappingPoint());
	}
	
	@Test
	public void whenOneDimensionalDataAndGeneratedCorePoint_generateOverlappingPointReturnsAnotherPoint()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2.),
				   Arrays.asList(5.),
				   3,
				   6,
				   coreExamplesGenerator,
				   overlappingExamplesGenerator);
	
		when(overlappingExamplesGenerator.getNumber(-2., 8.)).thenReturn(3., -9.);
		assertEquals(new Point(Arrays.asList(-9.)), shape.generateOverlappingPoint());
	}
	
	@Test
	public void whenTwoDimensionalData_generateOverlappingPointReturnsAppropriatePoint()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2.,25.),
				   Arrays.asList(5.,10.),
				   3,
				   6,
				   coreExamplesGenerator,
				   overlappingExamplesGenerator);
	
		when(overlappingExamplesGenerator.getNumber(-2., 8.)).thenReturn(4.);
		when(overlappingExamplesGenerator.getNumber(25., 13.)).thenReturn(17.);
		assertEquals(new Point(Arrays.asList(4.,17.)), shape.generateOverlappingPoint());
	}
	
	@Test
	public void whenTwoDimensionalDataAndGeneratedPointIsOutsideOverlapping_generateOverlappingPointReturnsAnotherPoint()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2.,25.),
				   Arrays.asList(5.,10.),
				   3,
				   6,
				   coreExamplesGenerator,
				   overlappingExamplesGenerator);
	
		when(overlappingExamplesGenerator.getNumber(-2., 8.)).thenReturn(4.);
		when(overlappingExamplesGenerator.getNumber(25., 13.)).thenReturn(15., 17.);
		assertEquals(new Point(Arrays.asList(4.,17.)), shape.generateOverlappingPoint());
	}
	
	@Test
	public void whenIsInOutlierForbiddenZone_returnsCorrectValue()
	{
		Shape shape = new HyperCircularData(Arrays.asList(-2.,25.),
										   Arrays.asList(5.,10.),
										   3,
										   6,
										   null,
										   null);
		
		assertTrue(shape.isInOutlierForbiddenZone(new Point(Arrays.asList(-2., 25.))));
		assertTrue(shape.isInOutlierForbiddenZone(new Point(Arrays.asList(-6., 34.))));
		assertTrue(shape.isInOutlierForbiddenZone(new Point(Arrays.asList(-8., 35.))));
		assertFalse(shape.isInOutlierForbiddenZone(new Point(Arrays.asList(-2., 45.))));
		assertFalse(shape.isInOutlierForbiddenZone(new Point(Arrays.asList(-12., 40.))));
	}
}
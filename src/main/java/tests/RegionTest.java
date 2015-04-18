package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import DataSetGenerator.Instance;
import DataSetGenerator.Point;
import DataSetGenerator.Region;
import DataSetGenerator.Rotator;
import DataSetGenerator.Shape;

public class RegionTest
{	
	private final static int numberOfTrainingExamples = 6;
	private final static int numberOfTestingExamples = -1;
	private final static double partOfOverlappingExamples = 0.2;
	private final static Point corePoint = new Point(Arrays.asList(2.,-3.)); 
	private final static Point rotatedCorePoint = new Point(Arrays.asList(-2.,3.)); 
	private final static Point overlappingPoint = new Point(Arrays.asList(200.,-300.));
	private final static Point rotatedOverlappingPoint = new Point(Arrays.asList(-200.,300.));
	private Shape shape = mock(Shape.class);
	private String classIndex = "Decision X";
	private Rotator rotator = mock(Rotator.class);
	private List<Rotator> rotators = Arrays.asList(rotator);
	
	public RegionTest()
	{
		when(shape.generateCorePoint()).thenReturn(corePoint);
		when(rotator.rotate(corePoint)).thenReturn(rotatedCorePoint);
		when(shape.generateOverlappingPoint()).thenReturn(overlappingPoint);
		when(rotator.rotate(overlappingPoint)).thenReturn(rotatedOverlappingPoint);
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenNegativeNumberOfExamples_throws()
	{		
		new Region(shape, -1, 0, partOfOverlappingExamples, classIndex, rotators);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenNegativePartOfOverlappingExamples_throws()
	{		
		new Region(shape, numberOfTrainingExamples, numberOfTestingExamples, -0.25, classIndex, rotators);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenPartOfOverlappingExamplesIsAboveOne_throws()
	{		
		new Region(shape, numberOfTrainingExamples, numberOfTestingExamples, 1.01, classIndex, rotators);
	}
	
	@Test
	public void whenNumberOfExamplesIsPositive_generatesAppropriatelyCoreExamples()
	{		
		Region region = new Region(shape, numberOfTrainingExamples, numberOfTestingExamples, 0, classIndex, rotators);
		List<Instance> instances = region.generateTrainingInstances();
		assertEquals(numberOfTrainingExamples, instances.size(), 0);
		for(Instance instance : instances)
		{
			assertEquals(rotatedCorePoint, instance.point);
			assertEquals(classIndex, instance.classIndex);
		}
	}
	
	@Test
	public void whenNumberOfExamplesIsPositive_generatesAppropriatelyOverlappingExamples()
	{		
		Region region = new Region(shape, numberOfTrainingExamples, numberOfTestingExamples, 1, classIndex, rotators);
		List<Instance> instances = region.generateTrainingInstances();
		assertEquals(numberOfTrainingExamples, instances.size(), 0);
		for(Instance instance : instances)
		{
			assertEquals(rotatedOverlappingPoint, instance.point);
			assertEquals(classIndex, instance.classIndex);
		}
	}
	
	@Test
	public void whenIsCoveredIsCalled_returnsTrue()
	{	
		Region region = new Region(shape, numberOfTrainingExamples, numberOfTestingExamples, 0, classIndex, rotators);
		when(rotator.undo(rotatedCorePoint)).thenReturn(corePoint);
		when(shape.isCovered(corePoint)).thenReturn(true);
		assertTrue(region.isCovered(rotatedCorePoint));
	}
	
	@Test
	public void whenIsCoveredIsCalled_returnsFalse()
	{	
		Region region = new Region(shape, numberOfTrainingExamples, numberOfTestingExamples, 0, classIndex, rotators);
		when(rotator.undo(rotatedCorePoint)).thenReturn(corePoint);
		when(shape.isCovered(corePoint)).thenReturn(false);
		assertFalse(region.isCovered(rotatedCorePoint));
	}
}


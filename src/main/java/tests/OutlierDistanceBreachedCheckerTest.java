package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import DataSetGenerator.Instance;
import DataSetGenerator.OutlierDistanceBreachedChecker;
import DataSetGenerator.Point;


public class OutlierDistanceBreachedCheckerTest
{
	private double interOutlierDistance = 1.8;
	private static final Point otherPoint = new Point(Arrays.asList(1.));
	private static final String classIndex = "decision1";
	private OutlierDistanceBreachedChecker checker = new OutlierDistanceBreachedChecker(interOutlierDistance);
	private Instance instance = new Instance(new Point(Arrays.asList(0.)), classIndex);
	
	public OutlierDistanceBreachedCheckerTest()
	{
		
	}
	
	@Test
	public void whenIsNoPreviouslyGenerated_returnsFalse()
	{
		assertFalse(checker.isInterOutlierDistanceBreached(instance,
														   Arrays.asList(new Instance(otherPoint,
		 		  																      "decision2")),
		 		  										   new ArrayList<Instance>()));
	}
	
	@Test
	public void whenPreviouslyGeneratedIsFromSameClass_returnsTrue()
	{
		assertTrue(checker.isInterOutlierDistanceBreached(instance,
														  Arrays.asList(new Instance(otherPoint,
		 		  																     classIndex)),
		 		  										  new ArrayList<Instance>()));
	}
	
	@Test
	public void whenPreviouslyGeneratedIsFromSameClassButTheDistanceAboveThreshold_returnsFalse()
	{
		assertFalse(checker.isInterOutlierDistanceBreached(instance,
														   Arrays.asList(new Instance(new Point(Arrays.asList(interOutlierDistance)),
		 		  										 						     classIndex)),
		 		  										   new ArrayList<Instance>()));
	}
	
	@Test
	public void whenPreviouslyGeneratedIsInSameGroup_returnsFalseIndependentlyToDistance()
	{
		List<Instance> instances = Arrays.asList(new Instance(otherPoint, classIndex));
		assertFalse(checker.isInterOutlierDistanceBreached(instance, instances, instances));
	}
}
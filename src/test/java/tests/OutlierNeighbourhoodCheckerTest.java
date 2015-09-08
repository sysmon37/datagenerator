package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import generator.Instance;
import generator.NearestNeighbourSelector;
import generator.OutlierNeighbourhoodChecker;
import generator.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class OutlierNeighbourhoodCheckerTest
{
	private NearestNeighbourSelector selector = mock(NearestNeighbourSelector.class);
	private int classIndex = 0;
	private List<Instance> instances = new ArrayList<Instance>();
	private Instance generated = new Instance(new Point(Arrays.asList(-66.)), classIndex);
	private OutlierNeighbourhoodChecker checker = new OutlierNeighbourhoodChecker(selector);
	private List<Instance> ownGroup = Arrays.asList(new Instance(new Point(Arrays.asList(100.)), classIndex));
	private List<Instance> differentClassInstances = Arrays.asList(new Instance(new Point(Arrays.asList(100.)), 1));
	
	public OutlierNeighbourhoodCheckerTest()
	{
		for(double i = 0 ; i < 100 ; ++i)
			instances.add(new Instance(new Point(Arrays.asList(i)), classIndex));
	}
	
	@Test
	public void whenCheckFirstTimeAndNoObjectFromSameClassIsInNeighbourhood_returnsfalse()
	{
		when(selector.getNeighbours(5, generated.getPoint(), instances)).thenReturn(differentClassInstances);
		assertFalse(checker.hasNeighbourFromClassNotBelongingToOutlier(generated, instances, new ArrayList<Instance>()));
	}
	
	@Test
	public void whenCheckFirstTimeAndObjectFromSameClassIsInNeighbourhood_returnsTrue()
	{
		when(selector.getNeighbours(5, generated.getPoint(), instances)).thenReturn(instances.subList(96, 100));	
		assertTrue(checker.hasNeighbourFromClassNotBelongingToOutlier(generated, instances, new ArrayList<Instance>()));
	}
	
	@Test
	public void whenCheckSecondTimeAndObjectFromSameGroupIsInNeighbourhood_returnsFalse()
	{
		when(selector.getNeighbours(5, generated.getPoint(), sum(instances, ownGroup))).thenReturn(ownGroup);	
		assertFalse(checker.hasNeighbourFromClassNotBelongingToOutlier(generated, instances, ownGroup));
	}
	
	@Test
	public void whenCheckSecondTimeAndNoObjectFromSameGroupIsInNeighbourhood_returnsTrue()
	{
		when(selector.getNeighbours(5, generated.getPoint(), sum(instances, ownGroup))).thenReturn(differentClassInstances);	
		assertTrue(checker.hasNeighbourFromClassNotBelongingToOutlier(generated, instances, ownGroup));
	}
	
	private List<Instance> sum(List<Instance> A, List<Instance> B)
	{
		List<Instance> result = new ArrayList<Instance>(A);
		result.addAll(B);
		return result;
	}
}
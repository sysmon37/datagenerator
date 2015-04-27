package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import generator.Instance;
import generator.NearestNeighbourSelector;
import generator.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class NearestNeighbourSelectorTest
{
	private List<Instance> instances = new ArrayList<Instance>();
	private int K = 5;
	private NearestNeighbourSelector selector = new NearestNeighbourSelector();

	public NearestNeighbourSelectorTest()
	{
		for(int i = 1 ; i <= 10 ; ++i)
			instances.add(new Instance(new Point(Arrays.asList(i*1., i*4.)), ""));
	}
	
	@Test
	public void whenEmptyListOfNeighboursIsProvided_returnsEmptyList()
	{
		List<Instance> nearest = selector.getNeighbours(K,
														new Point(Arrays.asList(0., 0.)),
														new ArrayList<Instance>());
		assertEquals(0, nearest.size());
	}
	
	@Test
	public void whenNeighboursAreProvided_returnsKNeighbours()
	{
		List<Instance> nearest = selector.getNeighbours(K,
														new Point(Arrays.asList(0., 0.)),
														instances);
		assertElementsAreIdentical(instances.subList(0, K), nearest);
	}
	
	@Test
	public void whenNeighboursAreProvided_returnsKNearestNeighbours()
	{
		List<Instance> nearest = selector.getNeighbours(K,
														new Point(Arrays.asList(11., 44.)),
														instances);
		assertElementsAreIdentical(instances.subList(instances.size() - K, instances.size()), nearest);
	}
	
	@Test
	public void whenDistancesAreEqual_returnsMoreThanKNearestNeighbours()
	{
		List<Instance> nearest = selector.getNeighbours(K,
														new Point(Arrays.asList(0., 19.125)),
														instances);
		assertElementsAreIdentical(instances.subList(1, 7), nearest);
	}
	
	private void assertElementsAreIdentical(List<Instance> expected, List<Instance> actual)
	{
		assertEquals(expected.size(), actual.size());
		for(Instance instance : actual)
			assertTrue(expected.contains(instance));
	}
}
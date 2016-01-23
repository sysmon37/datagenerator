package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class OutlierNeighbourhoodCheckerTest
{
	private NearestNeighbourSelector selector = mock(NearestNeighbourSelector.class);
	private int classIndex = 0;
	private List<Example> examples = new ArrayList<Example>();
	private Example generated = new Example(new Point(Arrays.asList(-66.)), classIndex);
	private OutlierNeighbourhoodChecker checker = new OutlierNeighbourhoodChecker(selector);
	private List<Example> ownGroup = Arrays.asList(new Example(new Point(Arrays.asList(100.)), classIndex));
	private List<Example> differentClassExamples = Arrays.asList(new Example(new Point(Arrays.asList(100.)), 1));
	
	public OutlierNeighbourhoodCheckerTest()
	{
		for(double i = 0 ; i < 100 ; ++i)
			examples.add(new Example(new Point(Arrays.asList(i)), classIndex));
	}
	
	@Test
	public void whenCheckFirstTimeAndNoObjectFromSameClassIsInNeighbourhood_returnsfalse()
	{
		when(selector.getNeighbours(5, generated, examples)).thenReturn(differentClassExamples);
		assertFalse(checker.hasNeighbourFromClassNotBelongingToOutlier(generated, examples, new ArrayList<Example>()));
	}
	
	@Test
	public void whenCheckFirstTimeAndObjectFromSameClassIsInNeighbourhood_returnsTrue()
	{
		when(selector.getNeighbours(5, generated, examples)).thenReturn(examples.subList(96, 100));	
		assertTrue(checker.hasNeighbourFromClassNotBelongingToOutlier(generated, examples, new ArrayList<Example>()));
	}
	
	@Test
	public void whenCheckSecondTimeAndObjectFromSameGroupIsInNeighbourhood_returnsFalse()
	{
		when(selector.getNeighbours(5, generated, sum(examples, ownGroup))).thenReturn(ownGroup);	
		assertFalse(checker.hasNeighbourFromClassNotBelongingToOutlier(generated, examples, ownGroup));
	}
	
	@Test
	public void whenCheckSecondTimeAndNoObjectFromSameGroupIsInNeighbourhood_returnsTrue()
	{
		when(selector.getNeighbours(5, generated, sum(examples, ownGroup))).thenReturn(differentClassExamples);	
		assertTrue(checker.hasNeighbourFromClassNotBelongingToOutlier(generated, examples, ownGroup));
	}
	
	private List<Example> sum(List<Example> A, List<Example> B)
	{
		List<Example> result = new ArrayList<Example>(A);
		result.addAll(B);
		return result;
	}
}
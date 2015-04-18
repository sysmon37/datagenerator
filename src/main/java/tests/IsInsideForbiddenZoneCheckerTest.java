package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import DataSetGenerator.Instance;
import DataSetGenerator.IsInsideForbiddenZoneChecker;
import DataSetGenerator.Point;
import DataSetGenerator.Region;


public class IsInsideForbiddenZoneCheckerTest
{
	private static final String classIndex = "decision1";
	private HashMap<String, List<Region>> regions = new HashMap<String, List<Region>>();
	private IsInsideForbiddenZoneChecker checker;
	private Instance instance = new Instance(new Point(Arrays.asList(.4, .7)), classIndex);
	private Region region = mock(Region.class);

	@Test
	public void whenInstancesClassHasNoRegions_returnsFalse()
	{
		checker = new IsInsideForbiddenZoneChecker(regions);
		assertFalse(checker.isInsideForbiddenZone(instance));
	}
	
	@Test
	public void whenInstanceIsCoveredByClassRegion_returnsTrue()
	{
		when(region.isInOutlierForbiddenZone(instance.point)).thenReturn(true);
		regions.put(classIndex, Arrays.asList(region));
		checker = new IsInsideForbiddenZoneChecker(regions);
		assertTrue(checker.isInsideForbiddenZone(instance));
	}
	
	@Test
	public void whenInstanceIsNotCoveredByClassRegion_returnsFalse()
	{
		when(region.isInOutlierForbiddenZone(instance.point)).thenReturn(false);
		regions.put(classIndex, Arrays.asList(region));
		checker = new IsInsideForbiddenZoneChecker(regions);
		assertFalse(checker.isInsideForbiddenZone(instance));
	}
}
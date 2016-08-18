package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import pl.poznan.put.cs.idss.generator.generation.Example;
import pl.poznan.put.cs.idss.generator.generation.IsInsideForbiddenZoneChecker;
import pl.poznan.put.cs.idss.generator.generation.Point;
import pl.poznan.put.cs.idss.generator.generation.RegionGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;


public class IsInsideForbiddenZoneCheckerTest
{
	private static final int classIndex = 0;
	private HashMap<Integer, List<RegionGenerator>> regions = new HashMap<>();
	private IsInsideForbiddenZoneChecker checker;
	private Example example = new Example(new Point(Arrays.asList(.4, .7)), classIndex);
	private RegionGenerator region = mock(RegionGenerator.class);

	@Test
	public void whenExampleClassHasNoRegions_returnsFalse()
	{
		checker = new IsInsideForbiddenZoneChecker(regions);
		assertFalse(checker.isInsideForbiddenZone(example));
	}
	
	@Test
	public void whenExampleIsCoveredByClassRegion_returnsTrue()
	{
		when(region.isInNoOutlierRange(example.getPoint())).thenReturn(true);
		regions.put(classIndex, Arrays.asList(region));
		checker = new IsInsideForbiddenZoneChecker(regions);
		assertTrue(checker.isInsideForbiddenZone(example));
	}
	
	@Test
	public void whenExampleIsNotCoveredByClassRegion_returnsFalse()
	{
		when(region.isInNoOutlierRange(example.getPoint())).thenReturn(false);
		regions.put(classIndex, Arrays.asList(region));
		checker = new IsInsideForbiddenZoneChecker(regions);
		assertFalse(checker.isInsideForbiddenZone(example));
	}
}
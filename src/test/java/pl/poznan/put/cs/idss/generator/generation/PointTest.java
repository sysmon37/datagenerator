package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import pl.poznan.put.cs.idss.generator.generation.Point;

import java.util.Arrays;

import org.junit.Test;



public class PointTest
{
	private static final Point five = new Point(Arrays.asList(5.));
	private static final Point zero = new Point(Arrays.asList(0.));

	@Test
	public void whenPointsIsLower_returnsTrue()
	{
		assertTrue(zero.isLowerOrEqual(zero));
	}
	
	@Test
	public void whenPointsIsNotLower_returnsFalse()
	{
		assertFalse(five.isLowerOrEqual(zero));
	}
	
	@Test
	public void whenMultidemensionalPointIsNotGreater_returnsFalse()
	{
		assertFalse(new Point(Arrays.asList(0., 5.)).isLowerOrEqual(new Point(Arrays.asList(5., 0.))));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void whenComparesDifferentDimensions_throws()
	{
		zero.isLowerOrEqual(new Point(Arrays.asList(5., 0.)));
	}
}


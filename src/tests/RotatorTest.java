package tests;

import static org.junit.Assert.assertEquals;
import generator.Point;
import generator.Rotator;

import java.util.Arrays;

import org.junit.Test;

public class RotatorTest
{	
	private final Point rotationPoint = new Point(Arrays.asList(15., -3., 2., 2.));

	@Test
	public void whenRotationFor180AngleAndWithoutShift_returnsRotatedPoint()
	{		
		Point middle = new Point(Arrays.asList(40., -105., 0., 0.));
		Point rotated = new Point(Arrays.asList(15., -3., -2., -2.0));
		Rotator rotator = new Rotator(middle, 180.0, 2, 3);
		assertEquals(rotated, rotator.rotate(rotationPoint));
	}
	
	@Test
	public void whenRotationFor135AngleAndWithoutShift_returnsRotatedPoint()
	{		
		Point middle = new Point(Arrays.asList(40., -105., 0., 0.));
		Point rotated = new Point(Arrays.asList(15., -3., -2. * Math.sqrt(2), 0.0));
		Rotator rotator = new Rotator(middle, 135.0, 2, 3);
		assertEquals(rotated, rotator.rotate(rotationPoint));
	}
	
	@Test
	public void whenRotationFor135AngleAndWitShift_returnsRotatedPoint()
	{		
		Point middle = new Point(Arrays.asList(40., -105., 4., 4.));
		Point rotated = new Point(Arrays.asList(15., -3., 4. + 2. * Math.sqrt(2), 4.0));
		Rotator rotator = new Rotator(middle, 135.0, 2, 3);
		assertEquals(rotated, rotator.rotate(rotationPoint));
	}
	
	@Test
	public void whenUndoIsCalled_returnsUnrotatedPoint()
	{		
		Point middle = new Point(Arrays.asList(40., -105., 4., 4.));
		Point rotated = new Point(Arrays.asList(15., -3., 4. + 2. * Math.sqrt(2), 4.0));
		Rotator rotator = new Rotator(middle, 135.0, 2, 3);
		assertEquals(rotationPoint, rotator.undo(rotated));
	}
}


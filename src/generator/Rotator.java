package generator;

import java.util.ArrayList;
import java.util.List;

public class Rotator
{
	private Point middle;
	private final double sinus;
	private final double cosinus;
	private int firstAxis;
	private int secondAxis;
	private double backwardCosinus;
	private double backwardSinus;

	public Rotator(Point middle, double angle, int firstAxis, int secondAxis)
	{
		this.middle = middle;
		double radian = getRadian(angle);
		this.sinus = Math.sin(radian);
		this.backwardSinus = Math.sin(-radian);
		this.cosinus = Math.cos(radian);
		this.backwardCosinus = Math.cos(-radian);
		this.firstAxis = firstAxis;
		this.secondAxis = secondAxis;
	}

	public double getRadian(double angle)
	{
		return angle / 180.0 * Math.PI;
	}

	public Point rotate(Point rotationPoint)
	{
		return rotate(rotationPoint, sinus, cosinus);
	}
	
	private Point rotate(Point rotationPoint, double sinus, double cosinus)
	{
		List<Double> rotatedCoordinates = new ArrayList<Double>();
		for(int i = 0 ; i < rotationPoint.getNumberOfDimensions() ; ++i)
			rotatedCoordinates.add(rotationPoint.getValue(i));
			
		double x = rotatedCoordinates.get(firstAxis) - middle.getValue(firstAxis);
		double y = rotatedCoordinates.get(secondAxis) - middle.getValue(secondAxis);
		double rotatedX = x * cosinus - y * sinus;
		double rotatedY = x * sinus + y * cosinus;
		
		rotatedCoordinates.set(firstAxis, rotatedX + middle.getValue(firstAxis));
		rotatedCoordinates.set(secondAxis, rotatedY + middle.getValue(secondAxis));
		return new Point(rotatedCoordinates);
	}

	public Point undo(Point rotated)
	{
		return rotate(rotated, backwardSinus, backwardCosinus);
	}

}

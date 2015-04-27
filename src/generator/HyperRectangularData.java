package generator;

import java.util.ArrayList;
import java.util.List;

public class HyperRectangularData extends Shape
{
	private RandomGenerator coreExamplesGenerator;
	private RandomGenerator overlappingExamplesGenerator;
	private double borderSize;

	private Point middlePoint;
	private List<Double> axisLengths;
	private double outlierForbiddenZone;
	
	public HyperRectangularData(List<Double> middleCoordinates,
								List<Double> axisLengths,
								double borderSize,
								double outlierForbiddenZone,
								RandomGenerator coreExamplesGenerator,
								RandomGenerator overlappingExamplesGenerator)
	{
		if(borderSize < 0)
			throw new IllegalArgumentException("Border size cannot be negative!");
		
		if(middleCoordinates.size() != axisLengths.size())
			throw new IllegalArgumentException("Coordinates size must be equal to number of axis!");
		
		middlePoint = new Point(middleCoordinates);			
		this.coreExamplesGenerator = coreExamplesGenerator;
		this.axisLengths = axisLengths;
		this.overlappingExamplesGenerator = overlappingExamplesGenerator;
		this.borderSize = borderSize;
		this.outlierForbiddenZone = outlierForbiddenZone;
	}
		
	public Point generateOverlappingPoint()
	{
		List<Double> newCoordinates = new ArrayList<Double>();
		for(int i = 0 ; i < middlePoint.getNumberOfDimensions() ; ++i)
			newCoordinates.add(overlappingExamplesGenerator.getNumber(middlePoint.getValue(i),
															   axisLengths.get(i) + borderSize));		
		Point point = new Point(newCoordinates);
		if(isCovered(point, 0))
		{
			return generateOverlappingPoint();
		}
		return point;
	}
	
	private boolean isCovered(Point point, double additionalMargin)
	{
		for(int i = 0 ; i < point.getNumberOfDimensions() ; ++i)
		{
			double middle = middlePoint.getValue(i);
			double axisLength = axisLengths.get(i);
			if(!isInsideInterval(point.getValue(i),
								 middle - axisLength - additionalMargin,
								 middle + axisLength + additionalMargin))
				return false;
		}
		return true;
	}
	
	private boolean isInsideInterval(Double value, double lowerBound, double upperBound)
	{
		return lowerBound <= value && value <= upperBound;
	}
	public Point generateCorePoint()
	{
		List<Double> newCoordinates = new ArrayList<Double>();
		for(int i = 0 ; i < middlePoint.getNumberOfDimensions() ; ++i)
			newCoordinates.add(coreExamplesGenerator.getNumber(middlePoint.getValue(i),
															  axisLengths.get(i)));
		return new Point(newCoordinates);
	}

	public boolean isCovered(Point point)
	{
		return isCovered(point, borderSize);
	}

	public boolean isInOutlierForbiddenZone(Point point)
	{
		return isCovered(point, borderSize + outlierForbiddenZone);
	}

	@Override
	public boolean isInCoreZone(Point point) {
		return isCovered(point, 0);
	}
}

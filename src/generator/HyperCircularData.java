package generator;

import java.util.ArrayList;
import java.util.List;

public class HyperCircularData extends Shape
{
	private Point middle;
	private List<Double> axisLengths;
	private RandomGenerator coreExamplesGenerator;
	private RandomGenerator overlappingExamplesGenerator;
	private double borderSize;
	private double outlierForbiddenZone;
	
	public HyperCircularData(List<Double> middleCoordinates,
							  List<Double> axisLengths,
							  double borderSize,
							  double outlierForbiddenZone,
							  RandomGenerator coreExamplesGenerator,
							  RandomGenerator overlappingExamplesGenerator)
	{
		if(borderSize < 0)
			throw new IllegalArgumentException("Border size cannot be negative! Actual it is: " + borderSize);
		this.middle = new Point(middleCoordinates);
		this.coreExamplesGenerator = coreExamplesGenerator;
		this.axisLengths = axisLengths;
		this.overlappingExamplesGenerator = overlappingExamplesGenerator;
		this.borderSize = borderSize;
		this.outlierForbiddenZone = outlierForbiddenZone;
	}

	@Override
	public Point generateCorePoint()
	{
		Point point = null;
		do
		{
			List<Double> coordinates = new ArrayList<Double>();
			for(int i = 0 ; i < middle.getNumberOfDimensions() ; ++i)
				coordinates.add(coreExamplesGenerator.getNumber(middle.getValue(i), axisLengths.get(i)));
			
			point = new Point(coordinates);
		}
		while(!isCovered(point, 0));
		return point;
	}
	
	@Override
	public Point generateOverlappingPoint()
	{
		Point point = null;
		do
		{
			List<Double> coordinates = new ArrayList<Double>();
			for(int i = 0 ; i < middle.getNumberOfDimensions() ; ++i)
				coordinates.add(overlappingExamplesGenerator.getNumber(middle.getValue(i), axisLengths.get(i) + borderSize));
			point = new Point(coordinates);
		}
		while(isCovered(point, 0) || !isCovered(point, borderSize));
		return point;
	}

	@Override
	public boolean isCovered(Point point)
	{
		return isCovered(point, borderSize);
	}

	private boolean isCovered(Point point, double additionalMargin)
	{
		double distance = 0;
		for(int i = 0 ; i < point.getNumberOfDimensions() ; ++i)
			distance += Math.pow(point.getValue(i) - middle.getValue(i), 2) /
						Math.pow(axisLengths.get(i) + additionalMargin, 2);
		return distance <= 1;
	}

	@Override
	public boolean isInOutlierForbiddenZone(Point point)
	{
		return isCovered(point, borderSize + outlierForbiddenZone);
	}

	@Override
	public boolean isInCoreZone(Point point) {
		return isCovered(point, 0);
	}

}

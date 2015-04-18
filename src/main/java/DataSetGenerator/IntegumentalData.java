package DataSetGenerator;

import java.util.List;

public class IntegumentalData extends Shape
{
	private Shape mainAreaData;
	private List<Region> figures;
	private int shapesIndex = 0;
	
	public IntegumentalData(List<Double> middleCoordinates,
			                List<Double> axisLengths,
			                RandomGenerator coreExamplesGenerator,
			                List<Region> figures
			                )
	{
		this.figures = figures;
		mainAreaData = new HyperRectangularData(middleCoordinates,
												axisLengths,
												0,
												0,
												coreExamplesGenerator,
												null);
	}

	public Point generateCorePoint()
	{
		Point point = null;
		do
		{
			point = mainAreaData.generateCorePoint();
		}
		while(isCoveredByAnyRegion(point));
		return point;
	}

	private boolean isCoveredByAnyRegion(Point point)
	{
		for(Region data : figures)
			if(data.isCovered(point))
				return true;
		return false;
	}

	@Override
	public boolean isCovered(Point point)
	{
		return false;
	}

	public Point generateOverlappingPoint()
	{
		Point point = figures.get(shapesIndex).generateOverlappingPoint();
		shapesIndex = (shapesIndex + 1) % figures.size();
		return point;
	}

	@Override
	public boolean isInOutlierForbiddenZone(Point point)
	{
		return false;
	}
}

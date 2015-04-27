package generator;

import java.util.ArrayList;
import java.util.List;

public class AdditionalOutlierPointGenerator implements PointGenerator
{
	private Point middlePoint;
	private static final int K_STANDARD_DEVIATION = 10;
	private RandomGenerator generator;
	private List<Double> deviations;
	
	public AdditionalOutlierPointGenerator(Point middlePoint,
				   NearestNeighbourSelector selector,
				   NeighbourhoodStandardDeviationCalculator deviationCalculator,
				   RandomGenerator generator,
				   List<Instance> instances)
	{
		this.middlePoint = middlePoint;
		this.generator = generator;
		deviations = getStandardDeviations(instances, selector, deviationCalculator);
	}
	
	private List<Double> getStandardDeviations(List<Instance> instances,
											   NearestNeighbourSelector selector,
											   NeighbourhoodStandardDeviationCalculator deviationCalculator)
	{
		List<Instance> nearest = selector.getNeighbours(K_STANDARD_DEVIATION, middlePoint, instances);
		return deviationCalculator.calculateStandardDeviations(nearest);
	}

	public Point generate()
	{
		List<Double> newCoordinates = new ArrayList<Double>();	
		for(int i = 0 ; i < middlePoint.getNumberOfDimensions() ; ++i)
			newCoordinates.add(generator.getNumber(middlePoint.getValue(i), deviations.get(i)));
		return new Point(newCoordinates);
	}
}

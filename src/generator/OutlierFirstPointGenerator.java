package generator;

import java.util.ArrayList;
import java.util.List;

public class OutlierFirstPointGenerator implements PointGenerator
{
	private List<Double> means = new ArrayList<Double>();
	private List<Double> deviations = new ArrayList<Double>();
	private RandomGenerator generator;
	
	public OutlierFirstPointGenerator(List<Double> minimumRanges,
			  						 List<Double> maximumRanges,
			  						 RandomGenerator generator)
	{
		if(minimumRanges.size() != maximumRanges.size())
			throw new IllegalArgumentException("MinimumRanges size: " + minimumRanges.size() +
											   "differs to maximumRanges size: " + maximumRanges.size());
		
		this.generator = generator;
		calculateBounds(minimumRanges, maximumRanges);
	}

	private void calculateBounds(List<Double> minimumRanges,
								 List<Double> maximumRanges)
	{
		for(int i = 0 ; i < minimumRanges.size() ; ++i)
		{
			double min = minimumRanges.get(i);
			double max = maximumRanges.get(i);
			
			if(min >= max)
				throw new IllegalArgumentException("Minimum range: " + min + " exceeds maximum: " + max);
			
			double range = (max - min);
			means.add(min + range/2);
			deviations.add(range/2);
		}
	}
	
	public Point generate()
	{
		List<Double> coordinates = new ArrayList<Double>();
		for(int i = 0 ; i < means.size() ; ++i)
			coordinates.add(generator.getNumber(means.get(i), deviations.get(i)));
		return new Point(coordinates);
	}
}

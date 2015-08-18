package generator;

import java.util.ArrayList;
import java.util.List;

public class NeighbourhoodStandardDeviationCalculator
{
	public List<Double> calculateStandardDeviations(List<Instance> instances)
	{
		checkInstancesSize(instances);
		final int dimensions = getNumberOfDimensions(instances);
		
		List<Double> deviations = new ArrayList<Double>();
		for(int i = 0 ; i < dimensions ; ++i)
			deviations.add(standardDeviationForDimension(instances, i));
		return deviations;
	}

	private double standardDeviationForDimension(List<Instance> instances, int dimension)
	{
		final double mean = calculateMean(instances, dimension);
		double variance = 0;
		
		for(Instance instance : instances)
			variance += Math.pow(instance.getPoint().getValue(dimension) - mean, 2);
			
		variance /= instances.size() - 1;
		return Math.sqrt(variance);
	}

	private double calculateMean(List<Instance> instances, int dimension) {
		double mean = 0;
		for(Instance instance : instances)
		{
			mean += instance.getPoint().getValue(dimension);
		}
		mean /= instances.size();
		return mean;
	}

	private void checkInstancesSize(List<Instance> instances) {
		if (instances.isEmpty())
			throw new IllegalArgumentException("Instances is empty!");
	}

	private int getNumberOfDimensions(List<Instance> instances)
	{
		final int dimensionality = instances.get(0).getPoint().getNumberOfDimensions();
		for(Instance instance : instances)
		{
			if(instance.getPoint().getNumberOfDimensions() != dimensionality)
				throw new IllegalArgumentException("Instances are located in different dimensions!");
		}
		return dimensionality;
	}
}

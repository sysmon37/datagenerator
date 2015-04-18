package Factories;

import java.util.List;

import DataSetGenerator.AdditionalOutlierPointGenerator;
import DataSetGenerator.Instance;
import DataSetGenerator.NearestNeighbourSelector;
import DataSetGenerator.NeighbourhoodStandardDeviationCalculator;

public class AdditionalPointGeneratorFactory
{
	public AdditionalOutlierPointGenerator createOutlier(List<Instance> instances,
																Instance generatedInstance)
	{
		return new AdditionalOutlierPointGenerator(generatedInstance.point,
				    				  new NearestNeighbourSelector(),
				    				  new NeighbourhoodStandardDeviationCalculator(),
				    				  RandomGeneratorFactory.makeOutliersGenerator(),
				    				  instances);
	}
}
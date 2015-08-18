package factories;

import generator.AdditionalOutlierPointGenerator;
import generator.Instance;
import generator.NearestNeighbourSelector;
import generator.NeighbourhoodStandardDeviationCalculator;

import java.util.List;

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
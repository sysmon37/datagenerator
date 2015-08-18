package generator;

import java.util.ArrayList;
import java.util.List;


public class DataSet
{
	private List<Region> regions;
	private Outlier outliersBuilder;
	
	public DataSet(List<Region> regions, Outlier outliersBuilder)
	{
		this.regions = regions;
		this.outliersBuilder = outliersBuilder;
	}
		
	public List<Instance> generateTrainingSet()
	{
		List<Instance> result = new ArrayList<>();
		for(Region region : regions)
			result.addAll(region.generateTrainingInstances());
		
		result.addAll(outliersBuilder.generate(result));
		
		return result;
	}

	public List<Instance> generateTestSet()
	{
		List<Instance> result = new ArrayList<>();
		for(Region region : regions)
			result.addAll(region.generateTestInstances());
		
		result.addAll(outliersBuilder.generateTest());
		
		return result;
	}
}
package factories;

import generator.DataSet;
import generator.Outlier;
import generator.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSetFactory
{
	public static DataSet create(double interOutlierDistance,
								 List<RegionDescription> regionDescriptions,
								 List<OutlierDescription> outlierDescriptions)
	{
		RegionsDependencyCreator regionsDependencyCreator = new RegionsDependencyCreator(regionDescriptions);
		Outlier outliersBuilder = OutlierBuilder.createOutlier(interOutlierDistance,
															   outlierDescriptions,
															   regionsDependencyCreator);
		return new DataSet(makeFlattened(regionsDependencyCreator.getRegions()), outliersBuilder);
	}
	
	private static List<Region> makeFlattened(Map<Integer, List<Region>> regions)
	{
		List<Region> result = new ArrayList<Region>();
		for(List<Region> lista : regions.values())
			result.addAll(lista);
		return result;
	}
}
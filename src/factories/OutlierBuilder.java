package factories;

import generator.IsInsideForbiddenZoneChecker;
import generator.NearestNeighbourSelector;
import generator.Outlier;
import generator.OutlierDistanceBreachedChecker;
import generator.OutlierFirstPointGenerator;
import generator.OutlierNeighbourhoodChecker;

import java.util.List;

class OutlierBuilder
{
	public static Outlier createOutlier(double interOutlierDistance,
										List<OutlierDescription> outlierDescriptions,
										RegionsDependencyCreator regionsDependencyCreator)
	{
		return new Outlier(outlierDescriptions,
						   new OutlierFirstPointGenerator(regionsDependencyCreator.getMinimumRanges(),
														  regionsDependencyCreator.getMaximumRanges(),
														  RandomGeneratorFactory.makeOverlappingExamplesGenerator()),
						   new IsInsideForbiddenZoneChecker(regionsDependencyCreator.getRegions()),
						   new OutlierDistanceBreachedChecker(interOutlierDistance),
						   new OutlierNeighbourhoodChecker(new NearestNeighbourSelector()),
						   new AdditionalPointGeneratorFactory());
	}
}
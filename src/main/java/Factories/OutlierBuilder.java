package Factories;

import java.util.List;

import DataSetGenerator.IsInsideForbiddenZoneChecker;
import DataSetGenerator.NearestNeighbourSelector;
import DataSetGenerator.Outlier;
import DataSetGenerator.OutlierDistanceBreachedChecker;
import DataSetGenerator.OutlierFirstPointGenerator;
import DataSetGenerator.OutlierNeighbourhoodChecker;

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
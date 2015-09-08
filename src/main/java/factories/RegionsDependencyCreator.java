package factories;

import generator.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RegionsDependencyCreator
{
	private final Map<Integer, List<Region>> regions = new HashMap<>();
	private final List<Double> mins = new ArrayList<>();
	private final List<Double> maxs = new ArrayList<>();

	public RegionsDependencyCreator(List<RegionDescription> regionDescriptions)
	{
		List<RegionDescription> integumentals = new ArrayList<>();
		List<Region> otherRegions = new ArrayList<>();
		
		for(RegionDescription c : regionDescriptions)
			if(c.shape == 'I')
				integumentals.add(c);
			else
			{
				Region region = RegionBuilder.build(c);
				addRegion(c.classIndex, region);
				otherRegions.add(region);
			}
		
		for(RegionDescription c : integumentals)
			addRegion(c.classIndex, RegionBuilder.buildIntegumental(c, new ArrayList<>(otherRegions)));
		
		calculateBorders(regionDescriptions);
	}
	
	private void addRegion(int classIndex, Region region)
	{
		if(!regions.containsKey(classIndex)) regions.put(classIndex, new ArrayList<>());
		regions.get(classIndex).add(region);
	}
	
	public Map<Integer, List<Region>> getRegions()
	{
		return regions;
	}
	
	private void calculateBorders(List<RegionDescription> regionDescriptions)
	{
		maxs.addAll(regionDescriptions.get(0).coordinates);
		mins.addAll(maxs);
		for(RegionDescription r : regionDescriptions)
		{
			for(int i = 0 ; i < maxs.size() ; ++i)
			{
				double newMax = r.coordinates.get(i) + r.lengths.get(i);
				double newMin = r.coordinates.get(i) - r.lengths.get(i);
				maxs.set(i, Math.max(maxs.get(i), newMax));
				mins.set(i, Math.min(mins.get(i), newMin));
			}
		}
	}

	public List<Double> getMinimumRanges()
	{
		return mins;
	}

	public List<Double> getMaximumRanges()
	{
		return maxs;
	}
}
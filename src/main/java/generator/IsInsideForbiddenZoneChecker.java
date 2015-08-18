package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class IsInsideForbiddenZoneChecker
{
	private Map<String, List<Region>> regions;
	public IsInsideForbiddenZoneChecker(Map<String, List<Region>> regions)
	{
		this.regions = regions;
	}
	
	public boolean isInsideForbiddenZone(Instance instance)
	{		
		for(Region r : regions.containsKey(instance.classIndex) ? regions.get(instance.classIndex) :
																  new ArrayList<Region>())
			if(r.isInOutlierForbiddenZone(instance.point))
				return true;
		return false;
	}
}
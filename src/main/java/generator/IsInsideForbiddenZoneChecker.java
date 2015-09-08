package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class IsInsideForbiddenZoneChecker
{
	private Map<Integer, List<Region>> regions;
	public IsInsideForbiddenZoneChecker(Map<Integer, List<Region>> regions)
	{
		this.regions = regions;
	}
	
	public boolean isInsideForbiddenZone(Instance instance)
	{		
		for(Region r : regions.containsKey(instance.getClassIndex()) ? regions.get(instance.getClassIndex()) :
																  new ArrayList<Region>())
			if(r.isInOutlierForbiddenZone(instance.getPoint()))
				return true;
		return false;
	}
}
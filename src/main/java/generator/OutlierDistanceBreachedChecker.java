package generator;

import java.util.List;

public class OutlierDistanceBreachedChecker
{
	private double interOutlierDistance;
	public OutlierDistanceBreachedChecker(double interOutlierDistance)
	{
		this.interOutlierDistance = interOutlierDistance;
	}
	
	public boolean isInterOutlierDistanceBreached(Instance generated,
												  List<Instance> outliers,
												  List<Instance> currentGroup)
	{
		for(Instance instance : outliers)
		{
			if(currentGroup.contains(instance))
				continue;
			
			double distance = generated.point.distance(instance.point);
			if((generated.classIndex.equals(instance.classIndex)) &&
			  (distance < interOutlierDistance))
			return true;
		}
		return false;
	}
}
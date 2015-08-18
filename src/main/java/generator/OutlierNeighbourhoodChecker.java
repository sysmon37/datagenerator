package generator;

import java.util.ArrayList;
import java.util.List;


public class OutlierNeighbourhoodChecker
{
	private NearestNeighbourSelector nearestNeighbourSelector;
	
	public OutlierNeighbourhoodChecker(NearestNeighbourSelector nearestNeighbourSelector)
	{
		this.nearestNeighbourSelector = nearestNeighbourSelector;
	}

	public boolean hasNeighbourFromClassNotBelongingToOutlier(Instance generated,
															  List<Instance> instances,
															  List<Instance> currentGroup)
	{
		List<Instance> copy = new ArrayList<Instance>(instances);
		copy.addAll(currentGroup);
		List<Instance> nearest = nearestNeighbourSelector.getNeighbours(5,
									  generated.getPoint(),
									  copy);
		int counter = 0;
		for(Instance neighbour : nearest)
			if (neighbour.getClassIndex().equals(generated.getClassIndex()))
			{
				if(!currentGroup.contains(neighbour))
					return true;
				++counter;
			}
		return currentGroup.size() > 0 ? counter == 0 : false;
	}
}
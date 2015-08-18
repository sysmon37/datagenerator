package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class NearestNeighbourSelector
{
	public List<Instance> getNeighbours(int K, Point target, List<Instance> instances)
	{
		TreeMap<Double, List<Instance>> map = new TreeMap<Double, List<Instance>>();
		for(Instance instance : instances)
			calculateDistancesAndGroupElement(K, target, map, instance);
		return extractKNearest(K, map);
	}

	private List<Instance> extractKNearest(int K, TreeMap<Double, List<Instance>> map)
	{
		List<Instance> nearest = new ArrayList<Instance>();
		for(List<Instance> values : map.values())
		{
			nearest.addAll(values);
			if(K <= nearest.size())
				break;
		}
		return nearest;
	}

	private void calculateDistancesAndGroupElement(int K,
												   Point target,
												   TreeMap<Double, List<Instance>> map,
												   Instance instance)
	{
		double distance = instance.getPoint().distance(target);
		if(K > map.size() || distance < map.lastKey())
			insert(map, instance, distance);
		removeSubsetsIfIsToMuch(K, map);
	}

	private void insert(TreeMap<Double, List<Instance>> map,
						Instance instance,
						double distance)
	{
		if(!map.containsKey(distance))
			map.put(distance, new ArrayList<Instance>());
		map.get(distance).add(instance);
	}

	private void removeSubsetsIfIsToMuch(int K, TreeMap<Double, List<Instance>> map)
	{
		if(K < map.size())
			map.remove(map.lastKey());
	}

}

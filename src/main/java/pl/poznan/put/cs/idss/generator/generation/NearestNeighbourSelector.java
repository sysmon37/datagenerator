package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class NearestNeighbourSelector
{
    public List<Example> getNeighbours(int K, Example target, List<Example> examples)
    {
        TreeMap<Double, List<Integer>> map = new TreeMap<Double, List<Integer>>();
        for (int i = 0 ; i < examples.size() ; ++i)
        {
        	Example example = examples.get(i);
            double distance = example.getPoint().distance(target.getPoint());
            calculateDistancesAndGroupElement(K, map, i, distance);
        }
        return extractKNearest(K, map, examples);
    }

    private List<Example> extractKNearest(int K,
    									  TreeMap<Double, List<Integer>> map,
    									  List<Example> examples)
    {
        List<Example> nearest = new ArrayList<Example>();
        for (List<Integer> values : map.values())
        {
            for(Integer i : values)
            	nearest.add(examples.get(i));
            if (K <= nearest.size())
                break;
        }
        return nearest;
    }

    private void calculateDistancesAndGroupElement(int K,
            TreeMap<Double, List<Integer>> map,
            int exampleIndex,
            double distance)
    {
        if (K > map.size() || distance < map.lastKey())
            insert(map, exampleIndex, distance);
        removeSubsetsIfIsTooMuch(K, map);
    }

    private void insert(TreeMap<Double, List<Integer>> map,
            int exampleIndex,
            double distance)
    {
        if (!map.containsKey(distance))
            map.put(distance, new ArrayList<Integer>());
        map.get(distance).add(exampleIndex);
    }

    private void removeSubsetsIfIsTooMuch(int K, TreeMap<Double, List<Integer>> map)
    {
        if (K < map.size())
            map.remove(map.lastKey());
    }
}

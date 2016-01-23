package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class NearestNeighbourSelector
{
    public List<Example> getNeighbours(int K, Example target, List<Example> examples)
    {
        TreeMap<Double, List<Example>> map = new TreeMap<Double, List<Example>>();
        for (Example example : examples)
            calculateDistancesAndGroupElement(K, target, map, example);
        return extractKNearest(K, map);
    }

    private List<Example> extractKNearest(int K, TreeMap<Double, List<Example>> map)
    {
        List<Example> nearest = new ArrayList<Example>();
        for (List<Example> values : map.values())
        {
            nearest.addAll(values);
            if (K <= nearest.size())
                break;
        }
        return nearest;
    }

    private void calculateDistancesAndGroupElement(int K,
            Example target,
            TreeMap<Double, List<Example>> map,
            Example example)
    {
        double distance = example.getPoint().distance(target.getPoint());
        if (K > map.size() || distance < map.lastKey())
            insert(map, example, distance);
        removeSubsetsIfIsTooMuch(K, map);
    }

    private void insert(TreeMap<Double, List<Example>> map,
            Example example,
            double distance)
    {
        if (!map.containsKey(distance))
            map.put(distance, new ArrayList<Example>());
        map.get(distance).add(example);
    }

    private void removeSubsetsIfIsTooMuch(int K, TreeMap<Double, List<Example>> map)
    {
        if (K < map.size())
            map.remove(map.lastKey());
    }
}

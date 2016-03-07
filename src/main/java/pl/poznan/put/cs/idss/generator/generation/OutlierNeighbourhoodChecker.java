package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
import java.util.List;

public class OutlierNeighbourhoodChecker {

    private NearestNeighbourSelector<Example> nearestNeighbourSelector;

    public OutlierNeighbourhoodChecker(NearestNeighbourSelector<Example> nearestNeighbourSelector) {
        this.nearestNeighbourSelector = nearestNeighbourSelector;
    }

    public boolean hasNeighbourFromClassNotBelongingToOutlier(Example generated,
            List<Example> examples,
            List<Example> currentGroup) {
        List<Example> copy = new ArrayList<Example>(examples);
        copy.addAll(currentGroup);
        List<Example> nearest = nearestNeighbourSelector.getNeighbours(5,
                generated,
                copy);
        int counter = 0;
        for (Example neighbour : nearest) {
            if (generated.getClassIndex() == neighbour.getClassIndex()) {
                if (!currentGroup.contains(neighbour)) {
                    return true;
                }
                ++counter;
            }
        }
        return currentGroup.size() > 0 ? counter == 0 : false;
    }
}

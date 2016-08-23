package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IsInsideForbiddenZoneChecker {

    private Map<Integer, List<RegionGenerator>> regions;

    public IsInsideForbiddenZoneChecker(Map<Integer, List<RegionGenerator>> regions) {
        this.regions = regions;
    }

    public boolean isInsideForbiddenZone(Example example) {
        for (RegionGenerator r : regions.containsKey(example.getClassIndex()) ? regions.get(example.getClassIndex())
                : new ArrayList<RegionGenerator>()) {
            if (r.isInNoOutlierRange(example.getPoint())) {
                return true;
            }
        }
        return false;
    }
}

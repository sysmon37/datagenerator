package pl.poznan.put.cs.idss.generator.generation;

import java.util.List;

public class OutlierDistanceBreachedChecker {

    private double interOutlierDistance;

    public OutlierDistanceBreachedChecker(double interOutlierDistance) {
        this.interOutlierDistance = interOutlierDistance;
    }

    public boolean isInterOutlierDistanceBreached(Example generated,
            List<Example> outliers,
            List<Example> currentGroup) {
        for (Example example : outliers) {
            if (currentGroup.contains(example)) {
                continue;
            }

            double distance = generated.getPoint().distance(example.getPoint());
            if ((generated.getClassIndex() == example.getClassIndex())
                    && (distance < interOutlierDistance)) {
                return true;
            }
        }
        return false;
    }
}

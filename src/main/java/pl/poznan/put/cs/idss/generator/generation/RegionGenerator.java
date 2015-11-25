package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Ratio;
import pl.poznan.put.cs.idss.generator.settings.Region;
import java.util.ArrayList;
import java.util.List;

public class RegionGenerator {

    private final int _classIndex;
    private final List<Rotator> _rotators;
    private final DataShape _dataShape;

    public RegionGenerator(DataShape shape,
            int classIndex,
            List<Rotator> rotators) {
        _dataShape = shape;
        _classIndex = classIndex;
        _rotators = rotators;
    }

    public Point generateOverlappingPoint() {
        return rotatePoint(_dataShape.generateOverlappingPoint());
    }

    public Point generateCorePoint() {
        return rotatePoint(_dataShape.generateCorePoint());
    }

    public List<Example> generateLearnExamples() {
        return generateExamples(Ratio.LEARN);
    }

    public List<Example> generateTestExamples() {
        return generateExamples(Ratio.TEST);
    }

    /**
     * Generates examples from a given set (learn or test).
     * 
     * @param setIndex -- @see Ratio#LEARN and @see Ratio#TEST
     * @return 
     */
    public List<Example> generateExamples(int setIndex) {
        List<Example> examples = new ArrayList<>();
        Region region = _dataShape.getRegion();
        
        // Generate borderline (overlapping) examples
        examples.addAll(generateOverlappingExamples(region.getNumExamples(setIndex, Ratio.BORDER)));
        // Generate safe (core) examples
        examples.addAll(generateCoreExamples(region.getNumExamples(setIndex, Ratio.SAFE)));
        return examples;
    }

    public boolean isCovered(Point point) {
        return _dataShape.isCovered(unrotatePoint(point));
    }

    public boolean isInNoOutlierZone(Point point) {
        return _dataShape.isInNoOutlierZone(unrotatePoint(point));
    }

    public boolean isInCoreZone(Point point) {
        return _dataShape.isInCoreZone(unrotatePoint(point));
    }

    protected Point unrotatePoint(Point point) {
        for (Rotator r : _rotators) {
            point = r.restore(point);
        }
        return point;
    }

    private List<Example> generateCoreExamples(int numExamples) {
        List<Example> examples = new ArrayList<>();
        while (examples.size() < numExamples) {
            Point point = generateCorePoint();
            examples.add(new Example(point, _classIndex, Example.Label.SAFE));
        }
        return examples;
    }

    private List<Point> generateCorePoints(int numExamples) {
        List<Point> points = new ArrayList<>();
        while (points.size() < numExamples) {
            points.add(generateCorePoint());
        }
        return points;
    }

    private List<Example> generateOverlappingExamples(int numExamples) {
        List<Example> examples = new ArrayList<>();
        while (examples.size() < numExamples) {
            Point point = generateOverlappingPoint();
            examples.add(new Example(point, _classIndex, Example.Label.BORDER));
        }
        return examples;
    }

    private List<Point> generateOverlappingPoints(int numExamples) {
        List<Point> points = new ArrayList<>();
        while (points.size() < numExamples) {
            points.add(generateOverlappingPoint());
        }
        return points;
    }

    private Point rotatePoint(Point point) {
        for (Rotator r : _rotators) {
            point = r.rotate(point);
        }
        return point;
    }

    private List<Example> toExampleList(List<Point> points) {
        List<Example> examples = new ArrayList<>();
        for (Point p : points) {
            examples.add(new Example(p, _classIndex));
        }
        return examples;
    }

}

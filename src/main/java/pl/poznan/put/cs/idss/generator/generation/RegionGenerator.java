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

    public Point generateBorderPoint() {
        return rotatePoint(_dataShape.generateBorderPoint());
    }

    public Point generateSafePoint() {
        return rotatePoint(_dataShape.generateSafePoint());
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
        examples.addAll(generateBorderExamples(region.getNumExamples(setIndex, Ratio.BORDER)));
        // Generate safe (core) examples
        examples.addAll(generateSafeExamples(region.getNumExamples(setIndex, Ratio.SAFE)));
        return examples;
    }

    public boolean isCovered(Point point) {
        return _dataShape.isCovered(unrotatePoint(point));
    }

    public boolean isInNoOutlierRange(Point point) {
        return _dataShape.isInNoOutlierRange(unrotatePoint(point));
    }

    public boolean isInSafeRange(Point point) {
        return _dataShape.isInSafeRange(unrotatePoint(point));
    }

    protected Point unrotatePoint(Point point) {
        for (Rotator r : _rotators) {
            point = r.restore(point);
        }
        return point;
    }

    private List<Example> generateSafeExamples(int numExamples) {
        List<Example> examples = new ArrayList<>();
        while (examples.size() < numExamples) {
            Point point = generateSafePoint();
            examples.add(new Example(point, _classIndex, Example.Label.SAFE));
        }
        return examples;
    }

    private List<Point> generateSafePionts(int numExamples) {
        List<Point> points = new ArrayList<>();
        while (points.size() < numExamples) {
            points.add(generateSafePoint());
        }
        return points;
    }

    private List<Example> generateBorderExamples(int numExamples) {
        List<Example> examples = new ArrayList<>();
        while (examples.size() < numExamples) {
            Point point = generateBorderPoint();
            examples.add(new Example(point, _classIndex, Example.Label.BORDER));
        }
        return examples;
    }

    private List<Point> generateBorderPoints(int numExamples) {
        List<Point> points = new ArrayList<>();
        while (points.size() < numExamples) {
            points.add(generateBorderPoint());
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

package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OutlierDistanceBreachedCheckerTest {

    private double interOutlierDistance = 1.8;
    private static final Point otherPoint = new Point(Arrays.asList(1.));
    private static final int classIndex = 0;
    private OutlierDistanceBreachedChecker checker = new OutlierDistanceBreachedChecker(interOutlierDistance);
    private Example example = new Example(new Point(Arrays.asList(0.)), classIndex);

    public OutlierDistanceBreachedCheckerTest() {

    }

    @Test
    public void whenIsNoPreviouslyGenerated_returnsFalse() {
        assertFalse(checker.isInterOutlierDistanceBreached(example,
                Arrays.asList(new Example(otherPoint,
                                1)),
                new ArrayList<Example>()));
    }

    @Test
    public void whenPreviouslyGeneratedIsFromSameClass_returnsTrue() {
        assertTrue(checker.isInterOutlierDistanceBreached(example,
                Arrays.asList(new Example(otherPoint,
                                classIndex)),
                new ArrayList<Example>()));
    }

    @Test
    public void whenPreviouslyGeneratedIsFromSameClassButTheDistanceAboveThreshold_returnsFalse() {
        assertFalse(checker.isInterOutlierDistanceBreached(example,
                Arrays.asList(new Example(new Point(Arrays.asList(interOutlierDistance)),
                                classIndex)),
                new ArrayList<Example>()));
    }

    @Test
    public void whenPreviouslyGeneratedIsInSameGroup_returnsFalseIndependentlyToDistance() {
        List<Example> examples = Arrays.asList(new Example(otherPoint, classIndex));
        assertFalse(checker.isInterOutlierDistanceBreached(example, examples, examples));
    }
}

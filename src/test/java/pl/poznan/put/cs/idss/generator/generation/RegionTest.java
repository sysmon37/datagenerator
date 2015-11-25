
package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Ratio;
import pl.poznan.put.cs.idss.generator.settings.Region;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import pl.poznan.put.cs.idss.generator.generation.Example;
import pl.poznan.put.cs.idss.generator.generation.Point;
import pl.poznan.put.cs.idss.generator.generation.RegionGenerator;
import pl.poznan.put.cs.idss.generator.generation.Rotator;
import pl.poznan.put.cs.idss.generator.generation.DataShape;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;



public class RegionTest {

    private final static int numberOfTrainingExamples = 6;
    private final static int numberOfTestingExamples = -1;
    private final static double partOfOverlappingExamples = 0.2;
    private final static Point corePoint = new Point(Arrays.asList(2., -3.));
    private final static Point rotatedCorePoint = new Point(Arrays.asList(-2., 3.));
    private final static Point overlappingPoint = new Point(Arrays.asList(200., -300.));
    private final static Point rotatedOverlappingPoint = new Point(Arrays.asList(-200., 300.));
    private DataShape shape = mock(DataShape.class);
    private Region region = mock(Region.class);
    private int classIndex = 0;
    private Rotator rotator = mock(Rotator.class);
    private List<Rotator> rotators = Arrays.asList(rotator);

    public RegionTest() {
        when(shape.generateCorePoint()).thenReturn(corePoint);
        when(rotator.rotate(corePoint)).thenReturn(rotatedCorePoint);
        when(shape.generateOverlappingPoint()).thenReturn(overlappingPoint);
        when(rotator.rotate(overlappingPoint)).thenReturn(rotatedOverlappingPoint);
    }

    @Test
    public void whenNumberOfExamplesIsPositive_generatesAppropriatelyCoreExamples() {

        RegionGenerator regionGenerator = new RegionGenerator(shape, classIndex, rotators);
        when(shape.getRegion()).thenReturn(region);
        when(region.getNumExamples(Ratio.LEARN)).thenReturn(numberOfTrainingExamples);
        when(region.getNumExamples(Ratio.TEST)).thenReturn(numberOfTestingExamples);
        when(region.getNumExamples(Ratio.LEARN, Ratio.SAFE)).thenReturn(numberOfTrainingExamples);
        when(region.getNumExamples(Ratio.LEARN, Ratio.BORDER)).thenReturn(0);
        when(region.getNumExamples(Ratio.TEST, Ratio.SAFE)).thenReturn(numberOfTestingExamples);
        when(region.getNumExamples(Ratio.TEST, Ratio.BORDER)).thenReturn(0);
        
        List<Example> examples = regionGenerator.generateLearnExamples();
        assertEquals(numberOfTrainingExamples, examples.size(), 0);
        for (Example example : examples) {
            assertEquals(rotatedCorePoint, example.getPoint());
            assertEquals(classIndex, example.getClassIndex());
        }
    }

    @Test
    public void whenNumberOfExamplesIsPositive_generatesAppropriatelyOverlappingExamples() {
        RegionGenerator regionGenerator = new RegionGenerator(shape, classIndex, rotators);
        when(shape.getRegion()).thenReturn(region);
        when(region.getNumExamples(Ratio.LEARN)).thenReturn(numberOfTrainingExamples);
        when(region.getNumExamples(Ratio.TEST)).thenReturn(numberOfTestingExamples);
        when(region.getNumExamples(Ratio.LEARN, Ratio.SAFE)).thenReturn(0);
        when(region.getNumExamples(Ratio.LEARN, Ratio.BORDER)).thenReturn(numberOfTrainingExamples);
        when(region.getNumExamples(Ratio.TEST, Ratio.SAFE)).thenReturn(0);
        when(region.getNumExamples(Ratio.TEST, Ratio.BORDER)).thenReturn(numberOfTestingExamples);
        List<Example> examples = regionGenerator.generateLearnExamples();
        assertEquals(numberOfTrainingExamples, examples.size(), 0);
        for (Example example : examples) {
            assertEquals(rotatedOverlappingPoint, example.getPoint());
            assertEquals(classIndex, example.getClassIndex());
        }
    }

    @Test
    public void whenIsCoveredIsCalled_returnsTrue() {
        RegionGenerator region = new RegionGenerator(shape, classIndex, rotators);
        when(rotator.restore(rotatedCorePoint)).thenReturn(corePoint);
        when(shape.isCovered(corePoint)).thenReturn(true);
        assertTrue(region.isCovered(rotatedCorePoint));
    }

    @Test
    public void whenIsCoveredIsCalled_returnsFalse() {
        RegionGenerator region = new RegionGenerator(shape, classIndex, rotators);
        when(rotator.restore(rotatedCorePoint)).thenReturn(corePoint);
        when(shape.isCovered(corePoint)).thenReturn(false);
        assertFalse(region.isCovered(rotatedCorePoint));
    }
}

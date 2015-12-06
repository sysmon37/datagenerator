package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Coordinate;
import pl.poznan.put.cs.idss.generator.settings.Distribution;
import pl.poznan.put.cs.idss.generator.settings.DistributionType;
import pl.poznan.put.cs.idss.generator.settings.Region;
import pl.poznan.put.cs.idss.generator.settings.ShapeType;
import pl.poznan.put.cs.idss.generator.settings.Size;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import pl.poznan.put.cs.idss.generator.generation.HyperRectangularDataShape;
import pl.poznan.put.cs.idss.generator.generation.Point;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;
import pl.poznan.put.cs.idss.generator.generation.DataShape;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class HyperRectangularDataTest {

    private static final int borderSize = 4;
    private List<Double> middleCoordinates = Arrays.asList(11.0, 170.5);
    private List<Double> axisLengths = Arrays.asList(6.0, 55.5);
    private RandomGenerator generator = mock(RandomGenerator.class);
    private List<Double> overlappingPointCoordinates = Arrays.asList(20.3, 143.14);
    private List<Double> corePointCoordinates = Arrays.asList(6.5, 140.1);
    private double outlierForbiddenZone = 20.;

    @Test
    public void whenGenerateCorePointIsCalled_returnsCorePoint() {
        when(generator.getNumbers()).
                thenReturn(Arrays.asList((corePointCoordinates.get(0) - middleCoordinates.get(0))/axisLengths.get(0),
                           (corePointCoordinates.get(1) - middleCoordinates.get(1))/axisLengths.get(1)));

        DataShape shape = new HyperRectangularDataShape(
                new Region(1, ShapeType.RECTANGLE, new Coordinate(middleCoordinates), new Size(axisLengths), borderSize, outlierForbiddenZone, new Distribution(DistributionType.UNIFORM), null),
                generator,
                null);

        assertEquals(new Point(corePointCoordinates), shape.generateCorePoint());
    }

    @Test
    public void whenGenerateOverlappingPointIsCalled_returnsOverlappingPoint() {
        when(generator.getNumbers()).
                thenReturn(Arrays.asList((overlappingPointCoordinates.get(0) - middleCoordinates.get(0))/(axisLengths.get(0) + borderSize),
                           (overlappingPointCoordinates.get(1) - middleCoordinates.get(1))/(axisLengths.get(1) + borderSize)));

        DataShape shape = new HyperRectangularDataShape(
                new Region(1, ShapeType.RECTANGLE, new Coordinate(middleCoordinates), new Size(axisLengths), borderSize, outlierForbiddenZone, new Distribution(DistributionType.UNIFORM), null),
                null,
                generator);

        assertEquals(new Point(overlappingPointCoordinates), shape.generateOverlappingPoint());
    }

    @Test
    public void whenGeneratedOverlappingPointIsInsideCore_returnsAnotherOneOverlappingPoint() {
        when(generator.getNumbers()).
                thenReturn(Arrays.asList((corePointCoordinates.get(0) - middleCoordinates.get(0))/(axisLengths.get(0) + borderSize),
                           (corePointCoordinates.get(1) - middleCoordinates.get(1))/(axisLengths.get(1) + borderSize)),
                           Arrays.asList((overlappingPointCoordinates.get(0) - middleCoordinates.get(0))/(axisLengths.get(0) + borderSize),
                		   (overlappingPointCoordinates.get(1) - middleCoordinates.get(1))/(axisLengths.get(1) + borderSize)));

        DataShape shape = new HyperRectangularDataShape(
                new Region(1, ShapeType.RECTANGLE, new Coordinate(middleCoordinates), new Size(axisLengths), borderSize, outlierForbiddenZone, new Distribution(DistributionType.UNIFORM), null),
                null,
                generator);

        assertEquals(new Point(overlappingPointCoordinates), shape.generateOverlappingPoint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenMiddlePointDimensionsDoesNotEqualsToAxisLengths_throws() {
        new HyperRectangularDataShape(
                new Region(1, ShapeType.RECTANGLE, new Coordinate(middleCoordinates), new Size(Arrays.asList(0.5)), 0.0, 0.0, new Distribution(DistributionType.UNIFORM), null),                
                null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenMarginIsNegative_throws() {
        new HyperRectangularDataShape(
                new Region(1, ShapeType.RECTANGLE, new Coordinate(middleCoordinates), new Size(axisLengths), -1, 0, new Distribution(DistributionType.UNIFORM), null),                
                null, null);
    }

    @Test
    public void whenIsCoverdCalled_returnsProperValue() {
        DataShape shape = new HyperRectangularDataShape(
                new Region(1, ShapeType.RECTANGLE, new Coordinate(middleCoordinates), new Size(axisLengths), borderSize, outlierForbiddenZone, new Distribution(DistributionType.UNIFORM), null),
                null,
                null);

        assertTrue(shape.isCovered(new Point(corePointCoordinates)));
        assertTrue(shape.isCovered(new Point(overlappingPointCoordinates)));

        assertFalse(shape.isCovered(new Point(Arrays.asList(1.0, 110.0))));
        assertFalse(shape.isCovered(new Point(Arrays.asList(0.5, 111.0))));
        assertFalse(shape.isCovered(new Point(Arrays.asList(21.2, 226.0))));
        assertFalse(shape.isCovered(new Point(Arrays.asList(17.0, 230.1))));
    }

    @Test
    public void whenIsInOutlierForbiddenZone_returnsCorrectValue() {
        DataShape shape = new HyperRectangularDataShape(
                new Region(1, ShapeType.RECTANGLE, new Coordinate(middleCoordinates), new Size(axisLengths), borderSize, outlierForbiddenZone, new Distribution(DistributionType.UNIFORM), null),
                null,
                null);
        assertTrue(shape.isInNoOutlierZone(new Point(corePointCoordinates)));
        assertFalse(shape.isInNoOutlierZone(new Point(Arrays.asList(41.0, 250.5))));
        assertTrue(shape.isInNoOutlierZone(new Point(overlappingPointCoordinates)));
        assertTrue(shape.isInNoOutlierZone(new Point(Arrays.asList(40.0, 245.))));
        assertFalse(shape.isInNoOutlierZone(new Point(Arrays.asList(42.0, 245.5))));
    }
}

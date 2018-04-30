package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;

import org.apache.commons.math3.random.MersenneTwister;
import pl.poznan.put.cs.idss.generator.settings.Coordinate;
import pl.poznan.put.cs.idss.generator.settings.Distribution;
import pl.poznan.put.cs.idss.generator.settings.DistributionType;
import pl.poznan.put.cs.idss.generator.settings.Region;
import pl.poznan.put.cs.idss.generator.settings.ShapeType;
import pl.poznan.put.cs.idss.generator.settings.Size;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import pl.poznan.put.cs.idss.generator.settings.BorderType;
import pl.poznan.put.cs.idss.generator.settings.Rotation;

public class HyperCircularDataTest {

    private RandomGenerator safeExamplesGenerator = mock(RandomGenerator.class);
    private RandomGenerator borderExamplesGenerator = mock(RandomGenerator.class);
    private AuxiliaryGenerator auxGenerator = mock(AuxiliaryGenerator.class);

    private List<Rotation> noRotations = new ArrayList<>();

    @Test(expected = IllegalArgumentException.class)
    public void whenNegativeBorderSize_throws() {
        new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(0.0)), new Size(Arrays.asList(1.0)), BorderType.FIXED, -1.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator);
    }

    @Test
    public void whenOneDimensionalDataAndCoreIsNormalized_returnsCorePoint() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(0.0)), new Size(Arrays.asList(1.0)), BorderType.FIXED, 0.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator,
                auxGenerator);
        shape.getRegion().updateRadiuses();
        when(safeExamplesGenerator.getNumbers()).thenReturn(Arrays.asList(-0.39));
        when(auxGenerator.getUniformNumber()).thenReturn(0.39);
        assertEquals(new Point(Arrays.asList(-0.39)), shape.generateSafePoint());
    }

    @Test
    public void whenOneDimensionalDataAndCoreIsShifted_returnsCorePoint() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0)), new Size(Arrays.asList(1.0)), BorderType.FIXED, 0.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator,
                auxGenerator);
        shape.getRegion().updateRadiuses();
        when(safeExamplesGenerator.getNumbers()).thenReturn(Arrays.asList(-0.39));
        when(auxGenerator.getUniformNumber()).thenReturn(0.25);
        assertEquals(new Point(Arrays.asList(-2.25)), shape.generateSafePoint());
    }

    @Test
    public void whenOneDimensionalDataAndCoreIsShiftedAndStretched_returnsCorePoint() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0)), new Size(Arrays.asList(5.0)), BorderType.FIXED, 0.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator,
                auxGenerator);
        shape.getRegion().updateRadiuses();
        when(safeExamplesGenerator.getNumbers()).thenReturn(Arrays.asList(-0.39));
        when(auxGenerator.getUniformNumber()).thenReturn(0.25);
        assertEquals(new Point(Arrays.asList(-3.25)), shape.generateSafePoint());
    }

    @Test
    public void whenCoreIsTwoDimensional_returnsCorePoint() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0, 25.0)), new Size(Arrays.asList(5.0, 10.0)), BorderType.FIXED, 0.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator,
                auxGenerator);
        shape.getRegion().updateRadiuses();
        when(safeExamplesGenerator.getNumbers()).thenReturn(Arrays.asList(-0.4, -0.3));
        when(auxGenerator.getUniformNumber()).thenReturn(0.64);
        assertEquals(new Point(Arrays.asList(-5.2, 20.2)), shape.generateSafePoint());
    }

    @Test
    public void whenOneDimensionalData_isCoveredReturnsTrueIfPointIsInCore() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0)), new Size(Arrays.asList(0.5)), BorderType.FIXED, 0.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator);
        shape.getRegion().updateRadiuses();
        assertTrue(shape.isCovered(new Point(Arrays.asList(-1.7))));
        assertFalse(shape.isCovered(new Point(Arrays.asList(0.3))));
        assertFalse(shape.isCovered(new Point(Arrays.asList(-3.0))));
    }

    @Test
    public void whenTwoDimensionalData_isCoveredReturnsTrueIfPointIsInCore() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0, 25.0)), new Size(Arrays.asList(5.0, 10.0)), BorderType.FIXED, 0.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator);
        shape.getRegion().updateRadiuses();
        assertTrue(shape.isCovered(new Point(Arrays.asList(-2., 35.))));
        assertTrue(shape.isCovered(new Point(Arrays.asList(-7., 25.))));
        assertFalse(shape.isCovered(new Point(Arrays.asList(-7., 26.))));
        assertFalse(shape.isCovered(new Point(Arrays.asList(-6., 32.))));
        assertTrue(shape.isCovered(new Point(Arrays.asList(-6., 30.))));
    }

    @Test
    public void whenBorderSizeIsNotEqualZero_isCoveredReturnsTrueIfPointIsInCoreOrOverlapping() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0, 25.0)), new Size(Arrays.asList(5.0, 10.0)), BorderType.FIXED, 3.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator);
        shape.getRegion().updateRadiuses();
        assertTrue(shape.isCovered(new Point(Arrays.asList(-2., 38.))));
        assertTrue(shape.isCovered(new Point(Arrays.asList(-10., 25.))));
        assertFalse(shape.isCovered(new Point(Arrays.asList(-10., 26.))));
        assertFalse(shape.isCovered(new Point(Arrays.asList(-7., 36.))));
        assertTrue(shape.isCovered(new Point(Arrays.asList(-7., 35.))));
    }

    @Test
    public void whenOneDimensionalData_generateOverlappingPointReturnsAppropriatePoint() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0)), new Size(Arrays.asList(5.0)), BorderType.FIXED, 3.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator,
                auxGenerator);

        shape.getRegion().updateRadiuses();
        when(borderExamplesGenerator.getNumbers()).thenReturn(Arrays.asList(0.75));
        when(auxGenerator.getUniformNumber()).thenReturn(0.8);
        assertEquals(new Point(Arrays.asList(4.4)), shape.generateBorderPoint());
    }

    @Test
    public void whenOneDimensionalDataAndGeneratedCorePoint_generateOverlappingPointReturnsAnotherPoint() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0)), new Size(Arrays.asList(5.0)), BorderType.FIXED, 3.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator,
                auxGenerator);

        shape.getRegion().updateRadiuses();
        when(borderExamplesGenerator.getNumbers()).thenReturn(Arrays.asList(0.625),
                Arrays.asList(-0.875));
        when(auxGenerator.getUniformNumber()).thenReturn(0.5, 0.75);
        assertEquals(new Point(Arrays.asList(-8.)), shape.generateBorderPoint());
    }

    @Test
    public void whenTwoDimensionalData_generateOverlappingPointReturnsAppropriatePoint() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0, 25.0)), new Size(Arrays.asList(5.0, 10.0)), BorderType.FIXED, 3.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator,
                auxGenerator);

        shape.getRegion().updateRadiuses();
        when(borderExamplesGenerator.getNumbers()).thenReturn(Arrays.asList(-0.4, 0.3));
        when(auxGenerator.getUniformNumber()).thenReturn(1.0);
        assertEquals(new Point(Arrays.asList(-8.4, 32.8)), shape.generateBorderPoint());
    }

    @Test
    public void whenTwoDimensionalDataAndGeneratedPointIsOutsideOverlapping_generateOverlappingPointReturnsAnotherPoint() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0, 25.0)), new Size(Arrays.asList(5.0, 10.0)), BorderType.FIXED, 3.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                safeExamplesGenerator,
                borderExamplesGenerator,
                auxGenerator);

        shape.getRegion().updateRadiuses();
        when(borderExamplesGenerator.getNumbers()).thenReturn(Arrays.asList(0.4, 0.3),
                Arrays.asList(0.3, -0.4));
        when(auxGenerator.getUniformNumber()).thenReturn(0.16, 0.81);
        assertEquals(new Point(Arrays.asList(2.32, 15.64)), shape.generateBorderPoint());
    }

    @Test
    public void whenIsInOutlierForbiddenZone_returnsCorrectValue() {
        DataShape shape = new HyperCircularDataShape(
                new Region(1.0, ShapeType.CIRCLE, new Coordinate(Arrays.asList(-2.0, 25.0)), new Size(Arrays.asList(5.0, 10.0)), BorderType.FIXED, 3.0, 6.0, new Distribution(DistributionType.UNIFORM), null),
                null,
                null);

        shape.getRegion().updateRadiuses();
        assertTrue(shape.isInNoOutlierRange(new Point(Arrays.asList(-2., 25.))));
        assertTrue(shape.isInNoOutlierRange(new Point(Arrays.asList(-6., 34.))));
        assertTrue(shape.isInNoOutlierRange(new Point(Arrays.asList(-8., 35.))));
        assertFalse(shape.isInNoOutlierRange(new Point(Arrays.asList(-2., 45.))));
        assertFalse(shape.isInNoOutlierRange(new Point(Arrays.asList(-12., 40.))));
    }
}

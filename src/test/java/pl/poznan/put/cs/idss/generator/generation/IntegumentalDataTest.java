package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Coordinate;
import pl.poznan.put.cs.idss.generator.settings.Distribution;
import pl.poznan.put.cs.idss.generator.settings.Region;
import pl.poznan.put.cs.idss.generator.settings.ShapeType;
import pl.poznan.put.cs.idss.generator.settings.Size;
import pl.poznan.put.cs.idss.generator.settings.DistributionType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import pl.poznan.put.cs.idss.generator.settings.BorderType;

public class IntegumentalDataTest {

    private final List<Double> middleCoordinates = Arrays.asList(11.0, 170.5);
    private final List<Double> axisLengths = Arrays.asList(6.0, 55.5);
    private final RandomGenerator generator = mock(RandomGenerator.class);
    private final List<RegionGenerator> figures = Arrays.asList(mock(RegionGenerator.class), mock(RegionGenerator.class));
    private final Region region = new Region(1.0, ShapeType.INTEGUMENTAL, new Coordinate(middleCoordinates), new Size(axisLengths), BorderType.FIXED, 0.0, 0.0, new Distribution(DistributionType.UNIFORM), null);
    private final DataShape shape = new IntegumentalDataShape(region.updateRadiuses(), generator, figures);
    private final List<Double> generatedCoordinates = Arrays.asList(-5.4, 33.1);

    @Test
    public void whenGenerateCorePointIsCalled_returnsCorePoint() {
        when(generator.getNumbers()).
                thenReturn(Arrays.asList((generatedCoordinates.get(0) - middleCoordinates.get(0))/axisLengths.get(0),
        				   (generatedCoordinates.get(1) - middleCoordinates.get(1))/axisLengths.get(1)));

        assertEquals(new Point(generatedCoordinates), shape.generateSafePoint());
    }

    @Test
    public void whenGeneratedCorePointIsInsideOtherShape_returnsAnotherCorePoint() {
        List<Double> skippedCoordinates = Arrays.asList(-51.4, 27.1);

        when(generator.getNumbers()).
                thenReturn(Arrays.asList((skippedCoordinates.get(0) - middleCoordinates.get(0))/axisLengths.get(0),
                		   (skippedCoordinates.get(1) - middleCoordinates.get(1))/axisLengths.get(1)), 
                		   Arrays.asList((generatedCoordinates.get(0) - middleCoordinates.get(0))/axisLengths.get(0),
                           (generatedCoordinates.get(1) - middleCoordinates.get(1))/axisLengths.get(1)));
        when(figures.get(0).isInSafeRange(new Point(skippedCoordinates))).
                thenReturn(true);

        assertEquals(new Point(generatedCoordinates), shape.generateSafePoint());
    }

    @Test
    public void whenGeneratesOverlappingPoint_returnsOverlappingPoint() {
        assertThatGeneratesOverlappingPointFromFirstFigure();
    }

    @Test
    public void whenGeneratesMoreOverlappingPoints_returnsThemSequentiallyFromFigures() {
        assertThatGeneratesOverlappingPointFromFirstFigure();

        List<Double> anotherCoordinates = Arrays.asList(-51.4, 27.1);
        when(figures.get(1).generateBorderPoint()).thenReturn(new Point(anotherCoordinates));
        assertEquals(new Point(anotherCoordinates), shape.generateBorderPoint());

        assertThatGeneratesOverlappingPointFromFirstFigure();
    }

    private void assertThatGeneratesOverlappingPointFromFirstFigure() {
        when(figures.get(0).generateBorderPoint()).thenReturn(new Point(generatedCoordinates));
        assertEquals(new Point(generatedCoordinates), shape.generateBorderPoint());
    }
}

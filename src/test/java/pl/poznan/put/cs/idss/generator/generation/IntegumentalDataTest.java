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
import pl.poznan.put.cs.idss.generator.generation.IntegumentalDataShape;
import pl.poznan.put.cs.idss.generator.generation.Point;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;
import pl.poznan.put.cs.idss.generator.generation.RegionGenerator;
import pl.poznan.put.cs.idss.generator.generation.DataShape;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class IntegumentalDataTest {

    private final List<Double> middleCoordinates = Arrays.asList(11.0, 170.5);
    private final List<Double> axisLengths = Arrays.asList(6.0, 55.5);
    private final RandomGenerator generator = mock(RandomGenerator.class);
    private final List<RegionGenerator> figures = Arrays.asList(mock(RegionGenerator.class), mock(RegionGenerator.class));
    private final DataShape shape = new IntegumentalDataShape(
            new Region(1, ShapeType.INTEGUMENTAL, new Coordinate(middleCoordinates), new Size(axisLengths), 0, 0, new Distribution(DistributionType.UNIFORM), null),
            generator, figures);
    private final List<Double> generatedCoordinates = Arrays.asList(-5.4, 33.1);

    @Test
    public void whenGenerateCorePointIsCalled_returnsCorePoint() {
        when(generator.getNumbers()).
                thenReturn(Arrays.asList((generatedCoordinates.get(0) - middleCoordinates.get(0))/axisLengths.get(0),
        				   (generatedCoordinates.get(1) - middleCoordinates.get(1))/axisLengths.get(1)));

        assertEquals(new Point(generatedCoordinates), shape.generateCorePoint());
    }

    @Test
    public void whenGeneratedCorePointIsInsideOtherShape_returnsAnotherCorePoint() {
        List<Double> skippedCoordinates = Arrays.asList(-51.4, 27.1);

        when(generator.getNumbers()).
                thenReturn(Arrays.asList((skippedCoordinates.get(0) - middleCoordinates.get(0))/axisLengths.get(0),
                		   (skippedCoordinates.get(1) - middleCoordinates.get(1))/axisLengths.get(1)), 
                		   Arrays.asList((generatedCoordinates.get(0) - middleCoordinates.get(0))/axisLengths.get(0),
                           (generatedCoordinates.get(1) - middleCoordinates.get(1))/axisLengths.get(1)));
        when(figures.get(0).isInCoreZone(new Point(skippedCoordinates))).
                thenReturn(true);

        assertEquals(new Point(generatedCoordinates), shape.generateCorePoint());
    }

    @Test
    public void whenGeneratesOverlappingPoint_returnsOverlappingPoint() {
        assertThatGeneratesOverlappingPointFromFirstFigure();
    }

    @Test
    public void whenGeneratesMoreOverlappingPoints_returnsThemSequentiallyFromFigures() {
        assertThatGeneratesOverlappingPointFromFirstFigure();

        List<Double> anotherCoordinates = Arrays.asList(-51.4, 27.1);
        when(figures.get(1).generateOverlappingPoint()).thenReturn(new Point(anotherCoordinates));
        assertEquals(new Point(anotherCoordinates), shape.generateOverlappingPoint());

        assertThatGeneratesOverlappingPointFromFirstFigure();
    }

    private void assertThatGeneratesOverlappingPointFromFirstFigure() {
        when(figures.get(0).generateOverlappingPoint()).thenReturn(new Point(generatedCoordinates));
        assertEquals(new Point(generatedCoordinates), shape.generateOverlappingPoint());
    }
}

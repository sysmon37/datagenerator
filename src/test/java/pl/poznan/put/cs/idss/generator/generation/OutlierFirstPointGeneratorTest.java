package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Coordinate;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import pl.poznan.put.cs.idss.generator.generation.OutlierFirstPointGenerator;
import pl.poznan.put.cs.idss.generator.generation.Point;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;

import java.util.Arrays;

import org.junit.Test;

public class OutlierFirstPointGeneratorTest {

    private final Coordinate minimumRanges = new Coordinate(Arrays.asList(-8., 8.2));
    private final Coordinate maximumRanges = new Coordinate(Arrays.asList(0.4, 90.2));
    private final RandomGenerator numberGenerator = mock(RandomGenerator.class);
    private final OutlierFirstPointGenerator pointGenerator = new OutlierFirstPointGenerator(minimumRanges,
            maximumRanges,
            numberGenerator);

    @Test
    public void whenGenerateIsCalled_returnsPoint() {
        when(numberGenerator.getNumber(-3.8, 4.20)).thenReturn(4.05);
        when(numberGenerator.getNumber(49.2, 41.0)).thenReturn(-3.4);
        assertEquals(new Point(Arrays.asList(4.05, -3.4)), pointGenerator.generate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenMinimumExceedsMaximum_throws() {
        new OutlierFirstPointGenerator(new Coordinate(Arrays.asList(0.4)), new Coordinate(Arrays.asList(0.2)), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenMinimumValuesHaveDifferenteSizeThanMaximumValues_throws() {
        new OutlierFirstPointGenerator(new Coordinate(Arrays.asList(0.1, 0.15)), new Coordinate(Arrays.asList(0.2)), null);
    }
}

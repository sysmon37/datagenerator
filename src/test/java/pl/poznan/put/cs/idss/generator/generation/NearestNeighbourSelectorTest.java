package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import pl.poznan.put.cs.idss.generator.generation.Example;
import pl.poznan.put.cs.idss.generator.generation.NearestNeighbourSelector;
import pl.poznan.put.cs.idss.generator.generation.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class NearestNeighbourSelectorTest {

    private List<Example> examples = new ArrayList<Example>();
    private int K = 5;
    private NearestNeighbourSelector selector = new NearestNeighbourSelector();

    public NearestNeighbourSelectorTest() {
        for (int i = 1; i <= 10; ++i) {
            examples.add(new Example(new Point(Arrays.asList(i * 1., i * 4.)), 0));
        }
    }

    @Test
    public void whenEmptyListOfNeighboursIsProvided_returnsEmptyList() {
        List<Example> nearest = selector.getNeighbours(K,
                new Example(new Point(Arrays.asList(0., 0.)), 59),
                new ArrayList<Example>());
        assertEquals(0, nearest.size());
    }

    @Test
    public void whenNeighboursAreProvided_returnsKNeighbours() {
        List<Example> nearest = selector.getNeighbours(K,
        		new Example(new Point(Arrays.asList(0., 0.)), 59),
                examples);
        assertElementsAreIdentical(examples.subList(0, K), nearest);
    }

    @Test
    public void whenNeighboursAreProvided_returnsKNearestNeighbours() {
        List<Example> nearest = selector.getNeighbours(K,
        		new Example(new Point(Arrays.asList(11., 44.)), 59),
                examples);
        assertElementsAreIdentical(examples.subList(examples.size() - K, examples.size()), nearest);
    }

    @Test
    public void whenDistancesAreEqual_returnsMoreThanKNearestNeighbours() {
        List<Example> nearest = selector.getNeighbours(K,
        		new Example(new Point(Arrays.asList(0., 19.125)), 59),
                examples);
        assertElementsAreIdentical(examples.subList(1, 7), nearest);
    }

    private void assertElementsAreIdentical(List<Example> expected, List<Example> actual) {
        assertEquals(expected.size(), actual.size());
        for (Example example : actual) {
            assertTrue(expected.contains(example));
        }
    }
}

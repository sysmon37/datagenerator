package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import pl.poznan.put.cs.idss.generator.settings.Ratio;

public class DataSetTest {

    private RegionGenerator shapeData = mock(RegionGenerator.class);
    private OutlierGenerator outlier = mock(OutlierGenerator.class);
    private DataSetGenerator dataSet = new DataSetGenerator(Arrays.asList(shapeData),
            outlier);
    private List<Example> examples = Arrays.asList(mock(Example.class),
            mock(Example.class),
            mock(Example.class),
            mock(Example.class));

    private List<Example> regionalExamples = examples.subList(0, 2);
    private List<Example> outliers = examples.subList(2, 4);

    @Test
    public void whenGenerateIsInvoked_generatedExamplesFromRegionsAndOutliers() {
        when(shapeData.generateExamples(Ratio.LEARN)).thenReturn(regionalExamples);
        when(outlier.generateExamples(Ratio.LEARN, regionalExamples)).thenReturn(outliers);
        assertEquals(examples, dataSet.generateLearnExamples());
        verify(shapeData, times(1)).generateExamples(anyInt());
        verify(outlier, times(1)).generateExamples(anyInt(), anyListOf(Example.class));
    }
}

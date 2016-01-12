package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.apache.commons.math3.random.RandomVectorGenerator;
import org.junit.Before;

import org.junit.Test;


public class LowDiscrepancySequenceGeneratorTest
{
	private static final int INTERVAL_LENGTH = 2;
	private static final int LOWER_BOUND = -1;
	private static final double PSEUDO_GENERATED_NUMBER = 0.44;
	private RandomVectorGenerator vectorGeneratorMock = null;
        private RandomGenerator generator = null;

    public LowDiscrepancySequenceGeneratorTest() {
    }
    
    @Before
    public void setUp() {
        vectorGeneratorMock = mock(RandomVectorGenerator.class);
        when(vectorGeneratorMock.nextVector()).thenReturn(new double[] { 0.0});
        generator = new LowDiscrepancySequenceGenerator(1, vectorGeneratorMock);
    
    }
    
    @Test
    public void whenCalledGetNumber_returnsProperValue()
    {
            double[] randomVector = {PSEUDO_GENERATED_NUMBER};
            when(vectorGeneratorMock.nextVector()).thenReturn(randomVector);
            assertEquals(Arrays.asList(LOWER_BOUND + PSEUDO_GENERATED_NUMBER * INTERVAL_LENGTH),
                                     generator.getNumbers());
    }
}
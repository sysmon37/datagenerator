package tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import DataSetGenerator.Instance;
import DataSetGenerator.DataSet;
import DataSetGenerator.Outlier;
import DataSetGenerator.Region;

public class DataSetTest
{	
	private Region shapeData = mock(Region.class);
	private Outlier outlier = mock(Outlier.class);
	private DataSet dataSet = new DataSet(Arrays.asList(shapeData),
										  outlier);
	private List<Instance> instances = Arrays.asList(mock(Instance.class),
												     mock(Instance.class),
												     mock(Instance.class),
												     mock(Instance.class));
	
	private List<Instance> regionalInstances = instances.subList(0, 2);
	private List<Instance> outliers = instances.subList(2, 4);
	
	@Test
	public void whenGenerateIsInvoked_generatedInstancesFromRegionsAndOutliers()
	{		
		when(shapeData.generateTrainingInstances()).thenReturn(regionalInstances);
		when(outlier.generate(regionalInstances)).thenReturn(outliers);
		assertEquals(instances, dataSet.generateTrainingSet());
		verify(shapeData, times(1)).generateTrainingInstances();
		verify(outlier, times(1)).generate(anyListOf(Instance.class));
	}
}

package tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import DataSetGenerator.AdditionalOutlierPointGenerator;
import DataSetGenerator.Instance;
import DataSetGenerator.IsInsideForbiddenZoneChecker;
import DataSetGenerator.Outlier;
import DataSetGenerator.OutlierDistanceBreachedChecker;
import DataSetGenerator.OutlierFirstPointGenerator;
import DataSetGenerator.OutlierNeighbourhoodChecker;
import DataSetGenerator.OutlierType;
import DataSetGenerator.Point;
import Factories.AdditionalPointGeneratorFactory;
import Factories.OutlierDescription;

public class OutlierTest
{
	private String classIndex = "first class";
	private OutlierDescription description = mock(OutlierDescription.class);
	private AdditionalOutlierPointGenerator additionaPointGenerator = mock(AdditionalOutlierPointGenerator.class);
	private OutlierFirstPointGenerator firstPointGenerator = mock(OutlierFirstPointGenerator.class);
	private List<Instance> instances = new ArrayList<Instance>();
	private Instance generatedInstance = new Instance(new Point(Arrays.asList(8.)), classIndex);
	private AdditionalPointGeneratorFactory additionalPointGeneratorFactory = mock(AdditionalPointGeneratorFactory.class);
	private Instance secondInstance = new Instance(new Point(Arrays.asList(9.0)), classIndex);
	private IsInsideForbiddenZoneChecker forbiddenZoneChecker = mock(IsInsideForbiddenZoneChecker.class);
	private OutlierDistanceBreachedChecker distanceChecker = mock(OutlierDistanceBreachedChecker.class);
	private OutlierNeighbourhoodChecker neighbourhoodChecker = mock(OutlierNeighbourhoodChecker.class);
	private Instance skippedInstance = new Instance(new Point(Arrays.asList(10.0)), classIndex);
	private Outlier outlier;
	
	public OutlierTest()
	{
		for(double i = 0 ; i < 100 ; ++i) 
			instances.add(new Instance(new Point(Arrays.asList(i)), classIndex));
		
		when(additionalPointGeneratorFactory.createOutlier(instances, generatedInstance)).thenReturn(additionaPointGenerator);
		when(firstPointGenerator.generate()).thenReturn(generatedInstance.point);
		when(additionaPointGenerator.generate()).thenReturn(secondInstance.point);
	}
	
	@Test
	public void whenGeneratesOutlier_returnsOutlier()
	{
		description = createOutlierDescription();
		outlier = new Outlier(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generatedInstance), outlier.generate(instances));
	}
	
	@Test
	public void whenGeneratesRareCases_returnsRareCases()
	{
		description = createRareCaseDescription();
		outlier = new Outlier(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generatedInstance, secondInstance), outlier.generate(instances));
	}
	
	@Test
	public void whenRareCaseMiddlePointIsInsideForbiddenZone_returnsAnotherRareCases()
	{
		when(firstPointGenerator.generate()).thenReturn(skippedInstance.point, generatedInstance.point);
		when(forbiddenZoneChecker.isInsideForbiddenZone(skippedInstance)).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new Outlier(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generatedInstance, secondInstance), outlier.generate(instances));		
	}
	
	@Test
	public void whenRareCasePointIsInsideForbiddenZone_returnsAnotherRareCases()
	{
		when(additionaPointGenerator.generate()).thenReturn(skippedInstance.point, secondInstance.point);
		when(forbiddenZoneChecker.isInsideForbiddenZone(skippedInstance)).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new Outlier(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generatedInstance, secondInstance), outlier.generate(instances));		
	}
	
	@Test
	public void whenRareCaseMiddleBreaksInterOutleirDistance_returnsAnotherRareCases()
	{
		when(firstPointGenerator.generate()).thenReturn(skippedInstance.point, generatedInstance.point);
		when(distanceChecker.isInterOutlierDistanceBreached(skippedInstance, new ArrayList<Instance>(), new ArrayList<Instance>())).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new Outlier(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generatedInstance, secondInstance), outlier.generate(instances));		
	}
	
	@Test
	public void whenRareCasePointBreaksInterOutleirDistance_returnsAnotherRareCases()
	{
		when(additionaPointGenerator.generate()).thenReturn(skippedInstance.point, secondInstance.point);
		when(distanceChecker.isInterOutlierDistanceBreached(skippedInstance, new ArrayList<Instance>(), Arrays.asList(generatedInstance))).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new Outlier(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generatedInstance, secondInstance), outlier.generate(instances));		
	}
	
	@Test
	public void whenRareCaseMiddleHasInvalidNeighbourhood_returnsAnotherRareCases()
	{
		when(firstPointGenerator.generate()).thenReturn(skippedInstance.point, generatedInstance.point);
		when(neighbourhoodChecker.hasNeighbourFromClassNotBelongingToOutlier(skippedInstance, instances, new ArrayList<Instance>())).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new Outlier(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generatedInstance, secondInstance), outlier.generate(instances));		
	}
	
	@Test
	public void whenRareCasePointHasInvalidNeighbourhood_returnsAnotherRareCases()
	{
		when(additionaPointGenerator.generate()).thenReturn(skippedInstance.point, secondInstance.point);
		when(neighbourhoodChecker.hasNeighbourFromClassNotBelongingToOutlier(skippedInstance, instances, Arrays.asList(generatedInstance))).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new Outlier(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generatedInstance, secondInstance), outlier.generate(instances));		
	}	
	
	@Test(expected = IllegalArgumentException.class)
	public void whenCannotGenerateValidPoint_throws()
	{
		when(firstPointGenerator.generate()).thenReturn(skippedInstance.point);
		when(neighbourhoodChecker.hasNeighbourFromClassNotBelongingToOutlier(skippedInstance, instances, new ArrayList<Instance>())).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new Outlier(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		outlier.generate(instances);		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenGeneratesTestWithoutGeneratingTraining_throws()
	{
		description = createRareCaseDescription();
		outlier = new Outlier(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		outlier.generateTest();
	}
	
	@Test
	public void whenGeneratesTest_returnsTestCases()
	{
		whenGeneratesRareCases_returnsRareCases();
		Instance testInstance = new Instance(new Point(Arrays.asList(11.0)), classIndex);
		when(additionaPointGenerator.generate()).thenReturn(testInstance.point);
		assertEquals(Arrays.asList(testInstance), outlier.generateTest());
	}
	
	@Test
	public void whenGeneratesTwoGroups_firstIsUsedInSecondsNeighbourhoodChecking()
	{
		description = createOutlierDescription();
		outlier = new Outlier(Arrays.asList(description, createOutlierDescription()),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		when(firstPointGenerator.generate()).thenReturn(generatedInstance.point, skippedInstance.point, secondInstance.point);
		when(neighbourhoodChecker.hasNeighbourFromClassNotBelongingToOutlier(skippedInstance, sum(instances, Arrays.asList(generatedInstance)), new ArrayList<Instance>())).thenReturn(true);
		assertEquals(Arrays.asList(generatedInstance, secondInstance), outlier.generate(instances));
	}
	
	@Test
	public void whenGeneratesTwoGroups_firstIsUsedInSecondsDistanceChecking()
	{
		description = createOutlierDescription();
		outlier = new Outlier(Arrays.asList(description, createOutlierDescription()),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		when(firstPointGenerator.generate()).thenReturn(generatedInstance.point, skippedInstance.point, secondInstance.point);
		when(distanceChecker.isInterOutlierDistanceBreached(skippedInstance, Arrays.asList(generatedInstance), new ArrayList<Instance>())).thenReturn(true);
		assertEquals(Arrays.asList(generatedInstance, secondInstance), outlier.generate(instances));
	}
	
	private List<Instance> sum(List<Instance> A, List<Instance> B)
	{
		List<Instance> result = new ArrayList<Instance>(A);
		result.addAll(B);
		return result;
	}
	
	private OutlierDescription createOutlierDescription()
	{
		return new OutlierDescription( OutlierType.OUTLIER, classIndex);
	}
	
	private OutlierDescription createRareCaseDescription()
	{
		return new OutlierDescription( OutlierType.RARE_CASE, classIndex);
	}
}
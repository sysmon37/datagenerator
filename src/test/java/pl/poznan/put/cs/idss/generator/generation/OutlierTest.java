package pl.poznan.put.cs.idss.generator.generation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import pl.poznan.put.cs.idss.generator.factories.AdditionalPointGeneratorFactory;
import pl.poznan.put.cs.idss.generator.factories.OutlierDescription;
import pl.poznan.put.cs.idss.generator.generation.AdditionalOutlierPointGenerator;
import pl.poznan.put.cs.idss.generator.generation.Example;
import pl.poznan.put.cs.idss.generator.generation.IsInsideForbiddenZoneChecker;
import pl.poznan.put.cs.idss.generator.generation.OutlierGenerator;
import pl.poznan.put.cs.idss.generator.generation.OutlierDistanceBreachedChecker;
import pl.poznan.put.cs.idss.generator.generation.OutlierFirstPointGenerator;
import pl.poznan.put.cs.idss.generator.generation.OutlierNeighbourhoodChecker;
import pl.poznan.put.cs.idss.generator.generation.OutlierType;
import pl.poznan.put.cs.idss.generator.generation.Point;

public class OutlierTest
{
	private int classIndex = 0;
	private OutlierDescription description = mock(OutlierDescription.class);
	private AdditionalOutlierPointGenerator additionaPointGenerator = mock(AdditionalOutlierPointGenerator.class);
	private OutlierFirstPointGenerator firstPointGenerator = mock(OutlierFirstPointGenerator.class);
	private List<Example> examples = new ArrayList<Example>();
	private Example generateExample = new Example(new Point(Arrays.asList(8.)), classIndex);
	private AdditionalPointGeneratorFactory additionalPointGeneratorFactory = mock(AdditionalPointGeneratorFactory.class);
	private Example secondExample = new Example(new Point(Arrays.asList(9.0)), classIndex);
	private IsInsideForbiddenZoneChecker forbiddenZoneChecker = mock(IsInsideForbiddenZoneChecker.class);
	private OutlierDistanceBreachedChecker distanceChecker = mock(OutlierDistanceBreachedChecker.class);
	private OutlierNeighbourhoodChecker neighbourhoodChecker = mock(OutlierNeighbourhoodChecker.class);
	private Example skippedExample = new Example(new Point(Arrays.asList(10.0)), classIndex);
	private OutlierGenerator outlier;
	
	public OutlierTest()
	{
		for(double i = 0 ; i < 100 ; ++i) 
			examples.add(new Example(new Point(Arrays.asList(i)), classIndex));
		
		when(additionalPointGeneratorFactory.create(examples, generateExample)).thenReturn(additionaPointGenerator);
		when(firstPointGenerator.generate()).thenReturn(generateExample.getPoint());
		when(additionaPointGenerator.generate()).thenReturn(secondExample.getPoint());
	}
	
	@Test
	public void whenGeneratesOutlier_returnsOutlier()
	{
		description = createOutlierDescription();
		outlier = new OutlierGenerator(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generateExample), outlier.generateLearnExamples(examples));
	}
	
	@Test
	public void whenGeneratesRareCases_returnsRareCases()
	{
		description = createRareCaseDescription();
		outlier = new OutlierGenerator(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generateExample, secondExample), outlier.generateLearnExamples(examples));
	}
	
	@Test
	public void whenRareCaseMiddlePointIsInsideForbiddenZone_returnsAnotherRareCases()
	{
		when(firstPointGenerator.generate()).thenReturn(skippedExample.getPoint(), generateExample.getPoint());
		when(forbiddenZoneChecker.isInsideForbiddenZone(skippedExample)).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new OutlierGenerator(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generateExample, secondExample), outlier.generateLearnExamples(examples));		
	}
	
	@Test
	public void whenRareCasePointIsInsideForbiddenZone_returnsAnotherRareCases()
	{
		when(additionaPointGenerator.generate()).thenReturn(skippedExample.getPoint(), secondExample.getPoint());
		when(forbiddenZoneChecker.isInsideForbiddenZone(skippedExample)).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new OutlierGenerator(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generateExample, secondExample), outlier.generateLearnExamples(examples));		
	}
	
	@Test
	public void whenRareCaseMiddleBreaksInterOutleirDistance_returnsAnotherRareCases()
	{
		when(firstPointGenerator.generate()).thenReturn(skippedExample.getPoint(), generateExample.getPoint());
		when(distanceChecker.isInterOutlierDistanceBreached(skippedExample, new ArrayList<Example>(), new ArrayList<Example>())).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new OutlierGenerator(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generateExample, secondExample), outlier.generateLearnExamples(examples));		
	}
	
	@Test
	public void whenRareCasePointBreaksInterOutleirDistance_returnsAnotherRareCases()
	{
		when(additionaPointGenerator.generate()).thenReturn(skippedExample.getPoint(), secondExample.getPoint());
		when(distanceChecker.isInterOutlierDistanceBreached(skippedExample, new ArrayList<Example>(), Arrays.asList(generateExample))).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new OutlierGenerator(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generateExample, secondExample), outlier.generateLearnExamples(examples));		
	}
	
	@Test
	public void whenRareCaseMiddleHasInvalidNeighbourhood_returnsAnotherRareCases()
	{
		when(firstPointGenerator.generate()).thenReturn(skippedExample.getPoint(), generateExample.getPoint());
		when(neighbourhoodChecker.hasNeighbourFromClassNotBelongingToOutlier(skippedExample, examples, new ArrayList<Example>())).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new OutlierGenerator(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generateExample, secondExample), outlier.generateLearnExamples(examples));		
	}
	
	@Test
	public void whenRareCasePointHasInvalidNeighbourhood_returnsAnotherRareCases()
	{
		when(additionaPointGenerator.generate()).thenReturn(skippedExample.getPoint(), secondExample.getPoint());
		when(neighbourhoodChecker.hasNeighbourFromClassNotBelongingToOutlier(skippedExample, examples, Arrays.asList(generateExample))).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new OutlierGenerator(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		assertEquals(Arrays.asList(generateExample, secondExample), outlier.generateLearnExamples(examples));		
	}	
	
	@Test(expected = IllegalArgumentException.class)
	public void whenCannotGenerateValidPoint_throws()
	{
		when(firstPointGenerator.generate()).thenReturn(skippedExample.getPoint());
		when(neighbourhoodChecker.hasNeighbourFromClassNotBelongingToOutlier(skippedExample, examples, new ArrayList<Example>())).thenReturn(true);
		description = createRareCaseDescription();
		outlier = new OutlierGenerator(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		outlier.generateLearnExamples(examples);		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenGeneratesTestWithoutGeneratingTraining_throws()
	{
		description = createRareCaseDescription();
		outlier = new OutlierGenerator(Arrays.asList(description),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		outlier.generateTestExamples();
	}
	
	@Test
	public void whenGeneratesTest_returnsTestCases()
	{
		whenGeneratesRareCases_returnsRareCases();
		Example testExample = new Example(new Point(Arrays.asList(11.0)), classIndex);
		when(additionaPointGenerator.generate()).thenReturn(testExample.getPoint());
                assertEquals(Arrays.asList(testExample), outlier.generateTestExamples());
	}
	
	@Test
	public void whenGeneratesTwoGroups_firstIsUsedInSecondsNeighbourhoodChecking()
	{
		description = createOutlierDescription();
		outlier = new OutlierGenerator(Arrays.asList(description, createOutlierDescription()),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		when(firstPointGenerator.generate()).thenReturn(generateExample.getPoint(), skippedExample.getPoint(), secondExample.getPoint());
		when(neighbourhoodChecker.hasNeighbourFromClassNotBelongingToOutlier(skippedExample, sum(examples, Arrays.asList(generateExample)), new ArrayList<Example>())).thenReturn(true);
		assertEquals(Arrays.asList(generateExample, secondExample), outlier.generateLearnExamples(examples));
	}
	
	@Test
	public void whenGeneratesTwoGroups_firstIsUsedInSecondsDistanceChecking()
	{
		description = createOutlierDescription();
		outlier = new OutlierGenerator(Arrays.asList(description, createOutlierDescription()),
							  firstPointGenerator,
							  forbiddenZoneChecker,
							  distanceChecker,
							  neighbourhoodChecker,
							  additionalPointGeneratorFactory);
		when(firstPointGenerator.generate()).thenReturn(generateExample.getPoint(), skippedExample.getPoint(), secondExample.getPoint());
		when(distanceChecker.isInterOutlierDistanceBreached(skippedExample, Arrays.asList(generateExample), new ArrayList<Example>())).thenReturn(true);
		assertEquals(Arrays.asList(generateExample, secondExample), outlier.generateLearnExamples(examples));
	}
	
	private List<Example> sum(List<Example> A, List<Example> B)
	{
		List<Example> result = new ArrayList<Example>(A);
		result.addAll(B);
		return result;
	}
	
	private OutlierDescription createOutlierDescription()
	{
		OutlierDescription od = new OutlierDescription( OutlierType.OUTLIER, classIndex);
                od.numTestExamples = 1;
                return od;
	}
	
	private OutlierDescription createRareCaseDescription()
	{
		OutlierDescription od = new OutlierDescription( OutlierType.RARE_CASE, classIndex);
                od.numTestExamples = 1;
                return od;
	}
}
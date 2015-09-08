package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cli.ParameterExtractor;
import factories.OutlierDescription;
import factories.RegionDescription;
import factories.RotationDescription;
import generator.OutlierType;

public class ParameterExtractorTest
{
	private final static int totalNumberOfExamples = 151; 
	private int numberOfClasses = 2;
	private int numberOfAttributes = 2;
	private String[] decision = {"1", "1"};
	private int[] regionProportionOfExamples = {78, 87};
	private int[] percentOfOverlappingExamples = {56, 55};
	private double[] coordinateX = {3.14, 2.78};
	private double[] coordinateY = {10.24, 20.48};
	private char[] shapes = {'C', 'R'};
	private double[] lengthX = {5, 5.5};
	private double[] lengthY = {61, 27.14};
	private double[] borderSize = {1.5, 33.4};
	private String[] distribution = {"N2", "U"};
	private int[] classImbalancedRatio = {111,223};
	private int[] classDistribution = {45, 94};
	private int[] numberOfRegionsPerClass = {0, 2};
	private int[] numberOfOutliersPerClass = {1, 1};
	private int[] numberOfRareCasesPerClass = {2, 3};
	private double interOutlierDistance = 0.83;
	private String[] outliersClass = {"0", "1", "0", "0", "1", "1", "1"};
	private OutlierType[] outliersTypes = {OutlierType.OUTLIER, OutlierType.OUTLIER,
										   OutlierType.RARE_CASE, OutlierType.RARE_CASE,
										   OutlierType.RARE_CASE, OutlierType.RARE_CASE,
										   OutlierType.RARE_CASE};
	@SuppressWarnings("unchecked")
	private List<List<String>> rotationParameters = Arrays.asList(Arrays.asList("2", "2", "1", "45.0"),
												 				  Arrays.asList("2", "1", "3", "70.0"));
	private double EPSILON = 0;
	private ParameterExtractor extractor = new ParameterExtractor(null);
	private double[] outlierForbiddenZone = {8.9, 3.7};
	private double testingExamplesSizeAsFractionOfTraining = 0.61;
	private int numberOfTrainingTestPairsToBeGenerated = 8;
	
//	@Test
	public void whenLoadsParameters_parsesThemCorrectly()
	{
		addHeaderToExtractor();
		assertEquals(numberOfAttributes, extractor.getNumberOfAttributes());
		assertEquals(numberOfClasses, extractor.getNumberOfClasses());
		assertEquals(totalNumberOfExamples, extractor.getTotalNumberOfExamples());
		assertEquals(testingExamplesSizeAsFractionOfTraining, extractor.getTestingExamplesSizeAsFractionOfTraining(), EPSILON);
		assertEquals(numberOfTrainingTestPairsToBeGenerated, extractor.getNumberOfTrainingTestPairsToBeGenerated());
		assertTrue(Arrays.equals(numberOfRegionsPerClass, extractor.getNumberOfRegionsPerClass()));

		extractor.add(prepareRegionDescription(0));
		extractor.add(prepareRegionDescription(1)); 
		extractor.add(Arrays.asList(""+numberOfOutliersPerClass[0],
							   	    ""+numberOfOutliersPerClass[1]));
		extractor.add(Arrays.asList(""+numberOfRareCasesPerClass[0],
		   	    					""+numberOfRareCasesPerClass[1]));
		extractor.add(Arrays.asList(""+interOutlierDistance));
		extractor.add(rotationParameters.get(0));
		extractor.add(rotationParameters.get(1));

		assertTrue(Arrays.equals(classDistribution, extractor.getAbsoluteDistributionOfRegionalExamplesForTraining()));		
		for(int i = 0 ; i < extractor.getCommands().size(); ++i)
		{
			RegionDescription command = extractor.getCommands().get(i);
			assertEquals(borderSize[i], command.borderSize, EPSILON);
			assertEquals(shapes[i], command.shape);	
			assertEquals(distribution[i], command.distribution);
			assertEquals(percentOfOverlappingExamples[i], command.partOfOverlappingExamples, EPSILON);
			assertEquals(i == 0 ? 44 : 50,
						 (int)command.numberOfTrainingExamples);
			assertEquals(coordinateX[i], command.coordinates.get(0), EPSILON);
			assertEquals(coordinateY[i], command.coordinates.get(1), EPSILON);
			assertEquals(decision[i], command.classIndex);
			assertEquals(lengthX[i], command.lengths.get(0), EPSILON);
			assertEquals(lengthY[i], command.lengths.get(1), EPSILON);
			assertEquals(outlierForbiddenZone[i], command.outlierForbiddenZone, EPSILON);
		}
		
		assertOutliers(extractor.getOutliers());
		assertEquals(interOutlierDistance, extractor.getInterOutlierDistance(), EPSILON);
		assertEquals(2, extractor.getCommands().size());
		assertRotations(extractor.getCommands().get(1));
		assertEquals(0, extractor.getCommands().get(0).rotations.size());
	}
	
	private void assertOutliers(List<OutlierDescription> outliers)
	{
		for(int i = 0 ; i < outliersClass.length ; ++i)
		{
			assertEquals(outliersClass[i], outliers.get(i).classIndex);
			assertEquals(outliersTypes[i], outliers.get(i).type);
		}
	}

	private void addHeaderToExtractor()
	{
		extractor.add(Arrays.asList(""+totalNumberOfExamples, ""+numberOfAttributes,
									""+numberOfClasses, ""+testingExamplesSizeAsFractionOfTraining, 
									""+numberOfTrainingTestPairsToBeGenerated, ""+classImbalancedRatio[0],
									""+classImbalancedRatio[1], 
									""+numberOfRegionsPerClass[0], ""+numberOfRegionsPerClass[1]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void whenThereIsToFewRegionsDescribed_throws()
	{
		addHeaderToExtractor();
		extractor.getCommands();
	}
	
	private void assertRotations(RegionDescription command)
	{
		List<RotationDescription> rotations = command.rotations;
		assertEquals(rotationParameters.size(), rotations.size());
		for(int i = 0 ; i < rotationParameters.size() ; ++i)
		{
			assertEquals(Integer.parseInt(rotationParameters.get(i).get(1)) - 1, rotations.get(i).rotationAxis[0]);
			assertEquals(Integer.parseInt(rotationParameters.get(i).get(2)) - 1, rotations.get(i).rotationAxis[1]);
			assertEquals(Double.parseDouble(rotationParameters.get(i).get(3)), rotations.get(i).angle, EPSILON);
		}
	}

	private List<String> prepareRegionDescription(int commandIndex)
	{
		return Arrays.asList(""+regionProportionOfExamples[commandIndex], ""+shapes[commandIndex],
							 ""+coordinateX[commandIndex], ""+coordinateY[commandIndex], ""+lengthX[commandIndex],
 							 ""+lengthY[commandIndex], ""+percentOfOverlappingExamples[commandIndex], ""+borderSize[commandIndex],
  				 			 ""+outlierForbiddenZone[commandIndex],distribution[commandIndex]);
	}
}


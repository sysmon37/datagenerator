package cli;

import java.io.IOException;
import java.util.List;
import DataSetGenerator.DataSet;
import DataSetGenerator.Instance;
import Factories.DataSetFactory;
import Factories.OutlierDescription;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		String path = args[0];
		InputStreamReader reader = new InputStreamReader(path);
		ParameterExtractor extractor = new ParameterExtractor();
		extractor.add(reader.readAll());	
		
		for(int i = 0 ; i < extractor.getNumberOfTrainingTestPairsToBeGenerated() ; ++i)
		{
			List<OutlierDescription> outlierDescriptions = extractor.getOutliers();
			DataSet dataSet = DataSetFactory.create(extractor.getInterOutlierDistance(),
													extractor.getCommands(),
													outlierDescriptions);
			List<Instance>[] instances = generateSets(dataSet);
			
			DataSetDescriptionCreator descriptionCreator = new DataSetDescriptionCreator(extractor,
																						 outlierDescriptions);
			String dataSetDescription = descriptionCreator.getDescription();
			System.out.println(dataSetDescription);
			String filename = removeExtension(path);
			new ArffWriter().writeToFile(filename + "_training_" + i + ".arff", instances[0], dataSetDescription);
			new ArffWriter().writeToFile(filename + "_test_" + i + ".arff", instances[1], "");
		}
	}
	
	private static String removeExtension(String filename)
	{
		int lastIndex = filename.lastIndexOf('.');
		if(lastIndex >= 0)
			return filename.substring(0, lastIndex);
		return filename;
	}

	private static List<Instance>[] generateSets(DataSet dataSet)
	{
		@SuppressWarnings("unchecked")
		List<Instance>[] instances = new List[11];
		instances[0] = dataSet.generateTrainingSet();
		instances[1] = dataSet.generateTestSet();
		return instances;
	}
}
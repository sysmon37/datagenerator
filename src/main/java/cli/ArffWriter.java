package cli;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import DataSetGenerator.Instance;

public class ArffWriter
{	
	public void writeToFile(String filename,
							List<Instance> instances,
							String dataSetDescription) throws IOException
	{
		FileWriter writer = new FileWriter(filename);	
		
		writer.write(prepareDescription(dataSetDescription)+"\n\n\n");
		writer.write("@relation New_artificial_problem\n" +
				 generateAttributes(instances.get(0).point.getNumberOfDimensions()) +
				 "@attribute Decision " + generateClasses(instances) + "\n" +
				 "@data\n");
		
		for(Instance instance : instances)
			writer.write(instance.toString() +'\n');

		writer.close();
	}
	
	private String generateClasses(List<Instance> instances)
	{
		HashSet<String> classes = new HashSet<String>();
		for(Instance i : instances)
			classes.add(i.classIndex);
		
		String description = classes.toString();
		return "{" + description.substring(1, description.length() - 1) +"}";
	}
	
	private String generateAttributes(int numberOfAttributes)
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0 ; i < numberOfAttributes ; ++i)
			result.append("@attribute Attribute_" + i + " numeric\n");
		return result.toString();
	}
	
	private String prepareDescription(String description)
	{
		StringBuilder result = new StringBuilder();
		for(String s : description.split("\n"))
			result.append("% " + s + "\n");
		return result.toString();
	}
}
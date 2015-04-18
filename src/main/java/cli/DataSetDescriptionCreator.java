package cli;

import java.util.Arrays;
import java.util.List;

import Factories.OutlierDescription;
import Factories.RandomGeneratorFactory;
import Factories.RegionDescription;
import Factories.RotationDescription;

public class DataSetDescriptionCreator
{
	private ParameterExtractor extractor;
	private List<OutlierDescription> outlierDescriptions;

	public DataSetDescriptionCreator(ParameterExtractor extractor,
									 List<OutlierDescription> outlierDescriptions)
	{
		this.extractor = extractor;
		this.outlierDescriptions = outlierDescriptions;
	}
	
	public String getDescription()
	{
		StringBuilder result = new StringBuilder();
		result.append(getRandomGeneratorSeed());
		result.append(getNumberOfExamples());
		result.append(getNumberOfAttributes());
		result.append(getNumberOfClasses());
		result.append(getClassDistribution());
		result.append(getRegionDescriptions());
		result.append(getOutlierDescriptions());
		return result.toString();
	}
	
	private String getRandomGeneratorSeed()
	{
		return "Random Generator's seed: " + RandomGeneratorFactory.RANDOM_SEED + "\n";
	}
	
	private String getOutlierDescriptions()
	{
		StringBuilder result = new StringBuilder();
		int order = 0;
		for(OutlierDescription o : outlierDescriptions)
		{
			result.append("Island " + order +
						  " contains " + o.type.getNumberOfTrainingExamples() + " examples," +
						  " belongs to " + o.classIndex +
						  " and is located about " + o.middle + ".\n");
			++order;
		}
		return result.toString();
	}

	private String getRegionDescriptions()
	{
		StringBuilder description = new StringBuilder();
		List<RegionDescription> regions = extractor.getCommands();
		int order = 0;
		for(RegionDescription r : regions)
		{
			description.append("Area " + order +
							   " has " + r.numberOfTrainingExamples + " examples " +
							   "in centre of " + r.coordinates +
							   ", axes with lengths: " + r.lengths +
							   ", where " + r.partOfOverlappingExamples +" of examples are located " +
							   "in the border of size " + r.borderSize + ".\n" +
							   "This area has " + getRegionDescription(r.shape) + " shape and " +
							   getDistributionDescription(r.distribution) + " distribution "+
							   "and belongs to class " + r.classIndex+".\n" +
							   getRotationDescription(r.rotations));
			++order;
		}
		return description.toString();
	}
	
	private String getRegionDescription(char type)
	{
		switch(type)
		{
			case 'C':
				return "hypercircular";
			case 'R':
				return "hyperrectangular";
			case 'I':
				return "integumental";
			default:
				throw new IllegalArgumentException(type + " is not a valid region type");
		}
	}
	
	private String getDistributionDescription(String type)
	{
		switch(type.charAt(0))
		{
			case 'U':
				return "uniform";
			case 'N':
				return "Gaussian with " + type.substring(1) + " standard deviations contained in area";
			default:
				throw new IllegalArgumentException(type + " is not a valid distribution type");
		}
	}

	private String getRotationDescription(List<RotationDescription> rotations)
	{
		StringBuilder result = new StringBuilder("It's axes are rotated as follows:\n");
		for(RotationDescription r : rotations)
			result.append("\t" + r.rotationAxis[0] +
					      " and " + r.rotationAxis[1] +
					      " are rotated for " + r.angle + " degrees\n");
		return result.toString();
	}

	private String getClassDistribution()
	{
		return "Number of examples per class: " + Arrays.toString(extractor.getAbsoluteDistributionOfRegionalExamplesForTraining()) + "\n";
	}

	private String getNumberOfClasses()
	{
		return "Number of classes: " + extractor.getNumberOfClasses() + "\n";
	}

	private Object getNumberOfAttributes()
	{
		return "Number of attributes: " + extractor.getNumberOfAttributes() + "\n";
	}

	private String getNumberOfExamples()
	{
		return "Total number of examples: " + extractor.getTotalNumberOfExamples() + "\n";
	}
}
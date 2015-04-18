package DataSetGenerator;

import java.util.ArrayList;
import java.util.List;


import Factories.AdditionalPointGeneratorFactory;
import Factories.OutlierDescription;

public class Outlier
{
	private List<OutlierDescription> outlierDescriptions;
	private OutlierFirstPointGenerator generator;
	private IsInsideForbiddenZoneChecker forbiddenZoneChecker;
	private OutlierDistanceBreachedChecker distanceChecker;
	private OutlierNeighbourhoodChecker neighbourhoodChecker;
	private int GENERATION_OUTLIER_TRIALS_NUMBER = 10000;
	private List<GeneratorAndInstance> pointGeneratorsAndItsInstances;
	private List<Instance> copyOfinstances;
	private List<Instance> outliers = new ArrayList<Instance>();
	private AdditionalPointGeneratorFactory additionalPointGeneratorFactory;
	
	public Outlier(List<OutlierDescription> outlierDescriptions,
				   OutlierFirstPointGenerator generator,
				   IsInsideForbiddenZoneChecker forbiddenZoneChecker,
 				   OutlierDistanceBreachedChecker distanceChecker,
 		 		   OutlierNeighbourhoodChecker neighbourhoodChecker,
				   AdditionalPointGeneratorFactory additionalPointGeneratorFactory)
	{
		this.outlierDescriptions = outlierDescriptions;
		this.generator = generator;
		this.forbiddenZoneChecker = forbiddenZoneChecker;
		this.distanceChecker = distanceChecker;
		this.neighbourhoodChecker = neighbourhoodChecker;
		this.additionalPointGeneratorFactory = additionalPointGeneratorFactory;
	}
		
	public List<Instance> generate(List<Instance> instances)
	{
		copyOfinstances = new ArrayList<Instance>(instances);
		pointGeneratorsAndItsInstances = new ArrayList<GeneratorAndInstance>();
		for(OutlierDescription outlierDescription : outlierDescriptions)
		{
			List<Instance> currentGroup = new ArrayList<Instance>();

			Instance firstInstance = generate(currentGroup,
											  outlierDescription.classIndex,
											  generator);
			currentGroup.add(firstInstance);
			outlierDescription.middle = firstInstance.point;
			
			AdditionalOutlierPointGenerator additionalPointGenerator = additionalPointGeneratorFactory.createOutlier(instances, firstInstance);

			
			while(currentGroup.size() < outlierDescription.type.getNumberOfTrainingExamples())
				currentGroup.add(generate(currentGroup,
										  outlierDescription.classIndex,
										  additionalPointGenerator));
			
			pointGeneratorsAndItsInstances.add(new GeneratorAndInstance(additionalPointGenerator, currentGroup));		
			outliers.addAll(currentGroup);
			copyOfinstances.addAll(currentGroup);
		}
		return outliers;
	}

	private Instance generate(List<Instance> currentGroup,
							  String classIndex,
							  PointGenerator generator)
	{	
		for(int z = 1 ; z <= GENERATION_OUTLIER_TRIALS_NUMBER ; ++z)
		{
			Instance generatedInstance = new Instance(generator.generate(), classIndex);
			if(isValid(generatedInstance, currentGroup))
				return generatedInstance;
		}
		throw new IllegalArgumentException("Cannot generate outlier!");
	}

	private boolean isValid(Instance generatedInstance,
							List<Instance> currentGroup)
	{
		return (!forbiddenZoneChecker.isInsideForbiddenZone(generatedInstance)) &&
		       (!distanceChecker.isInterOutlierDistanceBreached(generatedInstance, outliers, currentGroup)) &&
		       (!neighbourhoodChecker.hasNeighbourFromClassNotBelongingToOutlier(generatedInstance,
		    		   															 copyOfinstances,
		    		   															 currentGroup));
	}
	
	public List<Instance> generateTest()
	{
		if(pointGeneratorsAndItsInstances == null)
			throw new IllegalArgumentException("Generating test cases must be preceded by generating training cases!");
		List<Instance> result = new ArrayList<Instance>();
		for(GeneratorAndInstance item : pointGeneratorsAndItsInstances)
		{
			result.add(generate(item.instances,
								item.instances.get(0).classIndex,
								item.generator));
		}
		return result;
	}
}

class GeneratorAndInstance
{
	public PointGenerator generator;
	public List<Instance> instances;
	public GeneratorAndInstance(PointGenerator generator, List<Instance> instances)
	{
		this.generator = generator;
		this.instances = instances;
	}
}


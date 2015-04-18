package Factories;

import java.util.ArrayList;
import java.util.List;


public class RegionDescription
{
	public char shape;
	public String classIndex;
	public int imbalancedRatio;
	public double partOfOverlappingExamples;
	public double borderSize;
	public List<Double> coordinates = new ArrayList<Double>();
	public List<Double> lengths = new ArrayList<Double>();
	public String distribution;
	public List<RotationDescription> rotations = new ArrayList<RotationDescription>();
	public double outlierForbiddenZone;
	public Integer numberOfTrainingExamples;
	public Integer numberOfTestingExamples;
	
	public RegionDescription(List<String> subList, String classIndex)
	{
		imbalancedRatio = Integer.parseInt(subList.get(0));
		shape = subList.get(1).charAt(0);
		this.classIndex = classIndex;
		final int numberOfDimensions = (subList.size() - 5)/2 ;
				
		for(int i = 0 ; i < numberOfDimensions ; ++i)
		{
			coordinates.add(Double.parseDouble(subList.get(i + 2)));
		}
		for(int i = 0 ; i < numberOfDimensions ; ++i)
		{
			lengths.add(Double.parseDouble(subList.get(numberOfDimensions + i + 2)));
		}
		
		partOfOverlappingExamples = Double.parseDouble(subList.get(2*numberOfDimensions + 2));
		borderSize = Double.parseDouble(subList.get(2*numberOfDimensions + 3));
		outlierForbiddenZone = Double.parseDouble(subList.get(2*numberOfDimensions + 4));
		distribution = subList.get(2*numberOfDimensions + 5);
	}

	public boolean equals(Object object)
	{
		if(!(object instanceof RegionDescription))
			return false;
		throw new IllegalArgumentException("Needs to implement RegionDescription.equals()!");
	}
}

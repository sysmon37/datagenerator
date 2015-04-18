package DataSetGenerator;

import java.util.ArrayList;
import java.util.List;

public class Region
{
	public Region(Shape shape,
				  int numberOfTrainingExamples,
				  int numberOfTestingExamples,
				  double partOfOverlappingExamples,
				  String classIndex,
				  List<Rotator> rotators)
	{
		if(numberOfTrainingExamples < 0)
			throw new IllegalArgumentException("Number of examples cannot be negative!");
		
		if(partOfOverlappingExamples < 0)
			throw new IllegalArgumentException("Part Of Overlapping Examples cannot be negative!");

		if(partOfOverlappingExamples > 1)
			throw new IllegalArgumentException("Part Of Overlapping Examples cannot be greater than 1.0!");
		
		this.shape = shape;
		this.numberOfTestingExamples = numberOfTestingExamples;
		this.numberOfTrainingExamples = numberOfTrainingExamples;
		this.partOfOverlappingExamples = partOfOverlappingExamples;
		this.classIndex = classIndex;
		this.rotators = rotators;
	}
	
	public Point generateOverlappingPoint()
	{
		return rotatePoint(shape.generateOverlappingPoint());
	}
	
	public List<Instance> generateTrainingInstances()
	{
		return generateInstances(numberOfTrainingExamples);
	}
	
	public List<Instance> generateTestInstances()
	{
		return generateInstances(numberOfTestingExamples);
	}

	private List<Instance> generateInstances(int numberOfExamplesToBeGenerated)
	{
		List<Point> result = new ArrayList<Point>();
		result.addAll(generateOverlappingPoints(numberOfExamplesToBeGenerated));
		result.addAll(generateCorePoints(numberOfExamplesToBeGenerated));
		return toInstanceList(result);
	}
		
	public boolean isCovered(Point point)
	{
		for(Rotator r : rotators)
			point = r.undo(point);
		return shape.isCovered(point);
	}
	
	public boolean isInOutlierForbiddenZone(Point point)
	{
		for(Rotator r : rotators)
			point = r.undo(point);
		return shape.isInOutlierForbiddenZone(point);
	}
	
	private List<Point> generateCorePoints(int numberOfExamples)
	{
		int numberOfCoreExamples = (int)Math.round(numberOfExamples * (1 - partOfOverlappingExamples));
		List<Point> result = new ArrayList<Point>();
		while(result.size() < numberOfCoreExamples)
			result.add(rotatePoint(shape.generateCorePoint()));
		return result;
	}

	private List<Point> generateOverlappingPoints(int numberOfExamples)
	{
		int numberOfOverlappingExamples = (int)Math.round(numberOfExamples * partOfOverlappingExamples);
		List<Point> result = new ArrayList<Point>();
		while(result.size() < numberOfOverlappingExamples)
			result.add(generateOverlappingPoint());
		return result;
	}
	
	private Point rotatePoint(Point point)
	{
		for(Rotator r : rotators)
			point = r.rotate(point);
		return point;
	}
	
	private List<Instance> toInstanceList(List<Point> points)
	{
		List<Instance> result = new ArrayList<Instance>();
		for(Point p : points)
			result.add(new Instance(p, classIndex));
		return result;
	}
		
	private int numberOfTestingExamples;
	private int numberOfTrainingExamples;
	private double partOfOverlappingExamples;
	private String classIndex;
	private List<Rotator> rotators;
	private Shape shape;
}
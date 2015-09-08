package generator;

import java.util.ArrayList;
import java.util.List;

public class Region
{
	public Region(Shape shape,
				  int numberOfTrainingExamples,
				  int numberOfTestingExamples,
				  double partOfOverlappingExamples,
				  int classIndex,
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
	
	
        public Point generateOverlappingPoint() {
            return rotatePoint(shape.generateOverlappingPoint());
        }
        
        public Point generateCorePoint() {
            return rotatePoint(shape.generateCorePoint());
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
                List<Instance> instances = new ArrayList<>();
                // Generate borderline (overlapping) examples
                instances.addAll(generateOverlappingInstances(numberOfExamplesToBeGenerated));
                // Generate safe (core) examples
                instances.addAll(generateCoreInstances(numberOfExamplesToBeGenerated));
		return instances;
	}
		
	public boolean isCovered(Point point)
	{
//		for(Rotator r : rotators)
//			point = r.undo(point);
		return shape.isCovered(undoPoint(point));
	}
	
	public boolean isInOutlierForbiddenZone(Point point)
	{
//		for(Rotator r : rotators)
//			point = r.undo(point);
		return shape.isInOutlierForbiddenZone(undoPoint(point));
	}
	
	public boolean isInCoreZone(Point point) {
		return shape.isInCoreZone(undoPoint(point));
	}
	
	protected Point undoPoint(Point point) {
		for (Rotator r : rotators)
			point = r.undo(point);
		return point;
	}
	
	private List<Instance> generateCoreInstances(int numberOfExamples)
	{
		int numberOfCoreExamples = (int)Math.round(numberOfExamples * (1 - partOfOverlappingExamples));
		List<Instance> instances = new ArrayList<>();
		while(instances.size() < numberOfCoreExamples) {
			Point point = generateCorePoint();
                        instances.add(new Instance(point, classIndex, Instance.Label.SAFE));
                }
		return instances;
	}

        private List<Point> generateCorePoints(int numberOfExamples)
	{
		int numberOfCoreExamples = (int)Math.round(numberOfExamples * (1 - partOfOverlappingExamples));
		List<Point> result = new ArrayList<>();
		while(result.size() < numberOfCoreExamples)
			result.add(generateCorePoint());
		return result;
	}

	private List<Instance> generateOverlappingInstances(int numberOfExamples)
	{
		int numberOfOverlappingExamples = (int)Math.round(numberOfExamples * partOfOverlappingExamples);
		List<Instance> instances = new ArrayList<>();
		while(instances.size() < numberOfOverlappingExamples) {
                    Point point = generateOverlappingPoint();
                    instances.add(new Instance(point, classIndex, Instance.Label.BORDER));
                }
		return instances;
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
	private int classIndex;
	private List<Rotator> rotators;
	private Shape shape;
}
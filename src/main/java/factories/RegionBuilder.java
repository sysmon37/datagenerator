package factories;

import generator.HyperCircularData;
import generator.HyperRectangularData;
import generator.IntegumentalData;
import generator.Point;
import generator.RandomGenerator;
import generator.Region;
import generator.Rotator;
import generator.Shape;

import java.util.ArrayList;
import java.util.List;

public class RegionBuilder
{
	public static Region build(RegionDescription c)
	{
		RandomGenerator coreExamplesGenerator = RandomGeneratorFactory.makeCoreExamplesGenerator(c);
		RandomGenerator overlappingExamplesGenerator = RandomGeneratorFactory.makeOverlappingExamplesGenerator();
		Shape shape = null;
		switch(c.shape)
		{
			case 'C':
				shape = new HyperCircularData(c.coordinates,
											  c.lengths,
											  c.borderSize,
											  c.outlierForbiddenZone,
											  coreExamplesGenerator,
											  overlappingExamplesGenerator);
				break;
			case 'R':
				shape = new HyperRectangularData(c.coordinates,
												 c.lengths,														
												 c.borderSize,
												 c.outlierForbiddenZone,
												 coreExamplesGenerator,
												 overlappingExamplesGenerator);
				break;
			default:
				throw new IllegalArgumentException("Wrong option for shape!");
		}
		return new Region(shape,
						  c.numberOfTrainingExamples,
						  c.numberOfTestingExamples,
						  c.partOfOverlappingExamples,
						  c.classIndex,
						  createRotations(c));
	}

	public static Region buildIntegumental(RegionDescription c, List<Region> shapes)
	{
		Shape shape = new IntegumentalData(c.coordinates,
				                    c.lengths,
				                    RandomGeneratorFactory.makeOverlappingExamplesGenerator(),
				                    shapes);
		return new Region(shape,
						  c.numberOfTrainingExamples,
						  c.numberOfTestingExamples,
						  c.partOfOverlappingExamples,
						  c.classIndex,
						  new ArrayList<Rotator>());
	}
	
	private static List<Rotator> createRotations(RegionDescription c)
	{
		ArrayList<Rotator> result = new ArrayList<Rotator>();
		for(RotationDescription r : c.rotations)
			result.add(new Rotator(new Point(c.coordinates), r.angle, r.rotationAxis[0], r.rotationAxis[1]));
		return result;
	}
}
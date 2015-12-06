package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.random.RandomVectorGenerator;

public class LowDiscrepancySequenceGenerator extends RandomGenerator
{
	private RandomVectorGenerator randomVectorGenerator;
	public LowDiscrepancySequenceGenerator(Random generationAlgorithm,
										   int dimensionality,
										   RandomVectorGenerator randomVectorGenerator)
	{
		super(generationAlgorithm, dimensionality);
		this.randomVectorGenerator = randomVectorGenerator;
	}

	@Override
	protected double getNumber(double mean, double range)
	{
		throw new IllegalAccessError();
	}
	
	public List<Double> getNumbers()
	{
		List<Double> result = new ArrayList<Double>();
		for(double d : randomVectorGenerator.nextVector())
			result.add(d * 2 - 1);
		return result;
	}
	
}
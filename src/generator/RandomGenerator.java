package generator;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class RandomGenerator
{
	protected Random generationAlgorithm;

	public RandomGenerator(Random generationAlgorithm)
	{
		this.generationAlgorithm = generationAlgorithm;
	}
	
	public abstract double getNumber(double mean, double range);

	public void shuffle(List<Double> values)
	{
		Collections.shuffle(values, generationAlgorithm);
	}
}
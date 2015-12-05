package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
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
	
	protected abstract double getNumber(double mean, double range);

	public void shuffle(List<Double> values)
	{
		Collections.shuffle(values, generationAlgorithm);
	}
	
    public List<Double> getNumbers(int dimensionality)
    {
        List<Double> coordinates = new ArrayList<Double>();
        for (int i = 0; i < dimensionality; ++i)
            coordinates.add(getNumber(0, 1));
        return coordinates;
    }
}
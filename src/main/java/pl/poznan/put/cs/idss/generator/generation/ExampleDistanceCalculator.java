package pl.poznan.put.cs.idss.generator.generation;

public class ExampleDistanceCalculator implements DistanceCalculator<Example>
{
	public double calculate(Example example, Example target)
	{
		return example.getPoint().distance(target.getPoint());
	}
}
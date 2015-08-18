package generator;

import java.util.ArrayList;
import java.util.List;


public class Point
{
	private List<Double> coordinates = new ArrayList<Double>();

	public Point(List<Double> list)
	{
		this.coordinates.addAll(list);
	}
	
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		for(Double value: coordinates)
		{
			result.append(", " + value);
		}
		return result.substring(2);
	}
	
	public int getNumberOfDimensions()
	{
		return coordinates.size();
	}
	
	public Point add(double difference)
	{
		List<Double> result = new ArrayList<Double>(coordinates);
		for(int i = 0 ; i < result.size() ; ++i)
		{
			result.set(i, result.get(i) + difference);
		}
		return new Point(result);
	}
	
	public double getValue(int index)
	{
		return coordinates.get(index);
	}

	public boolean isLowerOrEqual(Point other)
	{
		checkDimensionality(other);
		
		for(int i = 0 ; i < coordinates.size() ; ++i)
			if (!(coordinates.get(i) <= other.coordinates.get(i)))
				return false;
		return true;
	}
	
	public boolean equals(Object object)
	{
		if(!(object instanceof Point))
			return false;
		
		Point other = (Point)object;
		checkDimensionality(other);
		
		for(int i = 0 ; i < coordinates.size() ; ++i)
			if (Math.abs(coordinates.get(i) - other.coordinates.get(i)) >= 0.00000001)
				return false;
		return true;	
	}

	private void checkDimensionality(Point other)
	{
		if(other.coordinates.size() != coordinates.size())
			throw new UnsupportedOperationException("Number of dimensions must be the same!" +
													"[this]=" + toString() + " and" +
													"[other] " + other.toString());
	}

	public double distance(Point other)
	{
		checkDimensionality(other);
		double sum = 0;
		for(int i = 0 ; i < getNumberOfDimensions() ; ++i)
			sum += Math.pow(getValue(i) - other.getValue(i), 2);
		return Math.sqrt(sum);
	}
}
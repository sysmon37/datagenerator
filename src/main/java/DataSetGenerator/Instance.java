package DataSetGenerator;


public class Instance
{
	public Point point;
	public String classIndex;

	public Instance(Point point, String classIndex)
	{
		this.point = point;
		this.classIndex = classIndex;
	}
		
	public String toString()
	{
		return point.toString() + ", " + classIndex;
	}
	
	public boolean equals(Object object)
	{
		if(!(object instanceof Instance))
			return false;
		
		Instance other = (Instance)object;
		return point.equals(other.point) && classIndex.equals(other.classIndex);	
	}
}
package factories;

import generator.OutlierType;
import generator.Point;

public class OutlierDescription
{
	public String classIndex;
	public OutlierType type;
	public Point middle;

	public OutlierDescription(OutlierType type, String classIndex)
	{
		this.classIndex = classIndex;
		this.type = type;		
	}
}
package factories;

import generator.OutlierType;
import generator.Point;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString @EqualsAndHashCode @Getter
public class OutlierDescription
{
	public int classIndex;
	public OutlierType type;
	public Point middle;

	public OutlierDescription(OutlierType type, int classIndex)
	{
		this.classIndex = classIndex;
		this.type = type;		
	}
}
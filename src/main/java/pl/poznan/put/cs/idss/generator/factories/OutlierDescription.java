package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.generation.OutlierType;
import pl.poznan.put.cs.idss.generator.generation.Point;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString @EqualsAndHashCode
public class OutlierDescription
{
	public int classIndex;
	public OutlierType type;
	public Point middle;
        
        public int numTestExamples = 0;
        public int numLearnExamples = 0;

	public OutlierDescription(OutlierType type, int classIndex)
	{
		this.classIndex = classIndex;
		this.type = type;	
                this.numLearnExamples = this.type.numLearnExamplesPerGroup();

	}
}
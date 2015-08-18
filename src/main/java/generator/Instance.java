package generator;

import lombok.Getter;
import lombok.Setter;


public class Instance
{
	@Getter private Point point;
	@Getter private String classIndex;
        
        public enum Label {
            SAFE,
            BORDER,
            RARE,
            OUTLIER, 
            DEFAULT
        };
        
        @Setter @Getter private Label label = Label.DEFAULT;
        

	public Instance(Point point, String classIndex)
	{
		this.point = point;
		this.classIndex = classIndex;
	}
		
        public Instance(Point point, String classIndex, Label label) {
            this(point, classIndex);
            this.label = label;
        }
        
	public String toString()
	{
		return point.toString() + ", " + classIndex;
	}
        
        public String toString(boolean addLabel) {
            StringBuilder sb = new StringBuilder(toString());
            if (addLabel) 
                sb.append(", ").append(getLabel().toString());
         
            return sb.toString();
        }
	
        public String toString(boolean addLabel, String labeledClassIndex) {
            StringBuilder sb = new StringBuilder(toString());
            if (addLabel) 
                sb.append(", ").append(getClassIndex().equals(labeledClassIndex) ? getLabel() : Label.DEFAULT);
            return sb.toString();
        }
        
        @Override
	public boolean equals(Object object)
	{
		if(!(object instanceof Instance))
			return false;
		
		Instance other = (Instance)object;
		return point.equals(other.point) && classIndex.equals(other.classIndex);	
	}
}
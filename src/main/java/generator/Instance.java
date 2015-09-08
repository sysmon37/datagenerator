package generator;

import lombok.Getter;
import lombok.Setter;


public class Instance
{
	@Getter private Point _point;
	@Getter private int _classIndex;
        
        public enum Label {
            SAFE,
            BORDER,
            RARE,
            OUTLIER, 
            DEFAULT
        };
        
        @Setter @Getter private Label _label = Label.DEFAULT;
        

	public Instance(Point point, int classIndex)
	{
		_point = point;
		_classIndex = classIndex;
	}
		
        public Instance(Point point, int classIndex, Label label) {
            this(point, classIndex);
            _label = label;
        }
        
	public String toString()
	{
		return getPoint().toString() + ", " + getClassIndex();
	}
        
        public String toString(boolean addLabel) {
            StringBuilder sb = new StringBuilder(toString());
            if (addLabel) 
                sb.append(", ").append(getLabel().toString());
         
            return sb.toString();
        }
	
        public String toString(boolean addLabel, int labeledClassIndex) {
            StringBuilder sb = new StringBuilder(toString());
            if (addLabel) 
                sb.append(", ").append(getClassIndex() == labeledClassIndex ? getLabel() : Label.DEFAULT);
            return sb.toString();
        }
        
        @Override
	public boolean equals(Object object)
	{
		if(!(object instanceof Instance))
			return false;
		
		Instance other = (Instance)object;
		return getPoint().equals(other.getPoint()) && getClassIndex()== other.getClassIndex();	
	}
}
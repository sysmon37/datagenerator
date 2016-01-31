package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.GeneratorSettings;
import lombok.Getter;
import lombok.Setter;

public class Example {

    @Getter
    private Point _point;
    @Getter
    private int _classIndex;

    public enum Label {

        SAFE,
        BORDER,
        RARE,
        OUTLIER,
        DEFAULT
    };

    @Setter
    @Getter
    private Label _label = Label.DEFAULT;

    public Example(Point point, int classIndex) {
        _point = point;
        _classIndex = classIndex;
    }

    public Example(Point point, int classIndex, Label label) {
        this(point, classIndex);
        _label = label;
    }

    @Override
    public String toString() {
        return toString(Integer.toString(_classIndex));
    }

    public String toString(boolean addLabel) {
        StringBuilder sb = new StringBuilder(toString());
        if (addLabel) {
            sb.append(", ").append(getLabel().toString());
        }

        return sb.toString();
    }

    public String toString(GeneratorSettings settings) {
        return toString(settings.getClassName(_classIndex));
    }
    
    public String toString(String classOrLabelName) {
        return _point.toString() + ", " + classOrLabelName;
    }
    
        
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Example)) {
            return false;
        }

        Example other = (Example) object;
        return getPoint().equals(other.getPoint()) && getClassIndex() == other.getClassIndex();
    }
}

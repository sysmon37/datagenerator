package pl.poznan.put.cs.idss.generator.settings;

import java.util.Formatter;
import java.util.Locale;
import lombok.Data;

@Data
public class Rotation {
        private final int _axis1;
        private final int _axis2;
        private final double _angle;
        
    @Override
    public String toString() {
        Formatter formatter = new Formatter(Locale.US);
        return formatter.format("(%d, %d) --> %s", _axis1, _axis2, _angle).toString();
    }
}

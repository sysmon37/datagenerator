package pl.poznan.put.cs.idss.generator.settings;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author swilk
 */
@EqualsAndHashCode
public class NotEmptyDoubleList {
    protected List<Double> _values = new ArrayList<>();

    public NotEmptyDoubleList(List<Double> values) {
        Validate.notNull(values);
        Validate.noNullElements(values);
        Validate.notEmpty(values);
        _values = new ArrayList<>(values);
    }

    public int size() {
        return _values.size();
    }

    public double get(int i) {
        return _values.get(i);
    }
    
    public List<Double> asList() {
        return new ArrayList<>(_values);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(get(i));
        }
        return sb.toString();
    }
    
}

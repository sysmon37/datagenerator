package config;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author swilk
 */
@ToString @EqualsAndHashCode
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
    
}

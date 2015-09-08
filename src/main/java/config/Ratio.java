package config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author swilk
 */
public class Ratio extends NotEmptyDoubleList {

    public final int RATIO_SAFE = 0;
    public final int RATIO_BORDER = 1;
    public final int RATIO_RARE = 2;
    public final int RATIO_OUTLIER = 3;
    
    public final int RATIO_LEARN = 0;
    public final int RATIO_TEST = 1;
            
    @Getter
    private double _total = 0;
    
    public Ratio(List<Double> values) {
        super(values);

        _total = 0;
        _values.stream().forEach((d) -> {
            _total += d;
        });
    }
    
    public Ratio toFractions() {
        List<Double> fractions = new ArrayList<>(size());
        if (getTotal() > 0) {
            for (int i = 0; i < size(); i++)
                fractions.add(_values.get(i)/getTotal());
        } else
            for (int i = 0; i < size(); i++)
                fractions.add(0.0);
        return new Ratio(fractions);
    }
    
    public Ratio subRatio(int startIndex, int size) {
        return new Ratio(_values.subList(startIndex, startIndex + size));
    }
    
    public List<Integer> sortIndexes() {
        return sortIndexes(true);
    }
    
    public List<Integer> sortIndexes(boolean ascending) {
        List<Integer> indexes = new ArrayList<>(size());
        for (int i = 0; i  < size(); i++)
            indexes.add(i);
        indexes.sort((i1, i2) -> ascending 
                ? Double.compare(get(i1), get(i2))
                : Double.compare(get(i2), get(i1)));
        return indexes;
    }

    
}

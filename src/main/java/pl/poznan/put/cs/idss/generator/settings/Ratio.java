package pl.poznan.put.cs.idss.generator.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author swilk
 */
public class Ratio extends NotEmptyDoubleList {

    public static final int SAFE = 0;
    public static final int BORDER = 1;
    public static final int RARE = 2;
    public static final int OUTLIER = 3;
    
    public static final List<Integer> TYPE_INDEXES = Arrays.asList(SAFE, BORDER, RARE, OUTLIER);
    
    public static final int LEARN = 0;
    public static final int TEST = 1;
    public static final List<Integer> SET_INDEXES = Arrays.asList(LEARN, TEST);
    
    public static final int SIZE_LEARN_TEST = 2;
    public static final int SIZE_EXAMPLE_TYPE = 4;
            
    @Getter
    private double _total = 0;

    public Ratio(int size, double value) {
        super(Collections.nCopies(size, value));
        _total = size * value;
    }
    
    public Ratio(List<Double> values) {
        super(values);

        _total = 0;
        _values.stream().forEach((d) -> {
            _total += d;
        });
    }
    
    public Ratio(Double... values) {
        this(Arrays.asList(values));
    }
    
    public Ratio(Ratio ratio) {
        this(ratio._values);
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

    public Ratio set(int index, double value) {
        _total -= _values.get(index) - value;
        _values.set(index, value);
        return this;       
    }
    
    @Override
    public String toString() {
        return StringUtils.join(_values, ":");
    }
}

package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.random.HaltonSequenceGenerator;
import org.apache.commons.math3.random.RandomVectorGenerator;


@Getter
public class LowDiscrepancySequenceGenerator extends RandomGenerator {

    private final RandomVectorGenerator _vectorGenerator;

    public LowDiscrepancySequenceGenerator(int numDimensions) {
        this(numDimensions, new HaltonSequenceGenerator(numDimensions));
    }
    
    public LowDiscrepancySequenceGenerator(int numDimensions, RandomVectorGenerator vectorGenerator) {
        super(numDimensions);
        Validate.validState(vectorGenerator != null);
        _vectorGenerator = vectorGenerator;
        Validate.isTrue(_vectorGenerator.nextVector().length == numDimensions);
        if (_vectorGenerator instanceof HaltonSequenceGenerator)
            ((HaltonSequenceGenerator) _vectorGenerator).skipTo(Math.abs(Math.abs(getNumberGenerator().nextInt())));
    }

    @Override
    protected double getNumber(double mean, double range) {
        throw new IllegalAccessError();
    }

    @Override
    public List<Double> getNumbers() {
        List<Double> result = new ArrayList<>();
        for (double d : _vectorGenerator.nextVector()) {
            result.add(d * 2 - 1);
        }
        return result;
    }

}

package pl.poznan.put.cs.idss.generator.generation;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;

@Getter
public abstract class RandomGenerator {

    protected int _numDimensions;

    public final static long RANDOM_SEED = generateRandomSeed();
    
    private BitsStreamGenerator _numberGenerator = null;
    
    private static long generateRandomSeed() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(SecureRandom.getSeed(Long.BYTES));
        buffer.flip();
        return buffer.getLong();
    }

    public RandomGenerator(int numDimensions) {
        this(numDimensions, new MersenneTwister(RANDOM_SEED));
    }
    
    public RandomGenerator(int numDimensions, BitsStreamGenerator numberGenerator) {
        _numDimensions = numDimensions;
        Validate.validState(numberGenerator != null);
        _numberGenerator = numberGenerator;
    }

    protected abstract double getNumber(double mean, double range);
    
    protected double getNumber() {
        return getNumber(0.0, 1.0);
    }

    public List<Double> getNumbers() {
        List<Double> coordinates = new ArrayList<>();
        for (int i = 0; i < _numDimensions; ++i) {
            coordinates.add(getNumber());
        }
        return coordinates;
    }
}

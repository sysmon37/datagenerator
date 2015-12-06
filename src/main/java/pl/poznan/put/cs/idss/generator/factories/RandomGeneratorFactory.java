package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.settings.Region;
import pl.poznan.put.cs.idss.generator.generation.GaussianDistributionGenerator;
import pl.poznan.put.cs.idss.generator.generation.HighQualityRandom;
import pl.poznan.put.cs.idss.generator.generation.RandomGenerator;
import pl.poznan.put.cs.idss.generator.generation.UniformDistributionGenerator;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;

public class RandomGeneratorFactory {

    public final static long RANDOM_SEED = generateRandomSeed();
    private static final Random random = new HighQualityRandom(RANDOM_SEED);//new SecureRandom();//new Random(RANDOM_SEED);

    private static long generateRandomSeed() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(SecureRandom.getSeed(Long.BYTES));
        buffer.flip();
        return buffer.getLong();
    }

    public static RandomGenerator createCoreExamplesGenerator(Region region) {
        switch (region.getDistribution().getType()) {
            case UNIFORM:
                return createOverlappingExamplesGenerator(region.getCenter().getNumDimensions());
            case NORMAL:
                return new GaussianDistributionGenerator(random,
                										 region.getCenter().getNumDimensions(),
                        region.getDistribution().getNumStandardDeviations());
        }
        throw new IllegalArgumentException("Wrong option for distribution!");
    }

    public static RandomGenerator createOverlappingExamplesGenerator(int dimensionality) {
        return new UniformDistributionGenerator(random, dimensionality);
    }

    public static RandomGenerator createOutliersGenerator(int dimensionality) {
        return new GaussianDistributionGenerator(random, dimensionality, 3.0);
    }
}

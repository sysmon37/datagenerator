package pl.poznan.put.cs.idss.generator.generation;

public class AuxiliaryGenerator extends RandomGenerator {

    public AuxiliaryGenerator() {
        super(1);
    }

    @Override
    protected double getNumber(double mean, double range) {
        return getNumberGenerator().nextDouble()*2.0*range - mean;
    }

    public double getUniformNumber() {
        return getNumberGenerator().nextDouble();
    }

    public double getGaussianNumber() {
        return getGaussianNumber(1);
    }

    public double getGaussianNumber(double numStDev) {
        double g;
        do
            g = Math.abs(getNumberGenerator().nextGaussian()/numStDev);
        while (g > 1.0);
        return g;
    }
}

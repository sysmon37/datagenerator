package pl.poznan.put.cs.idss.generator.generation;

import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import pl.poznan.put.cs.idss.generator.settings.*;

import java.util.List;
import org.apache.commons.lang3.Validate;

public class HyperCircularDataShape extends DataShape {

    private final RandomGenerator _safeGenerator;
    private final RandomGenerator _borderGenerator;
    private final AuxiliaryGenerator _auxGenerator;

    public HyperCircularDataShape(Region region,
            RandomGenerator safeGenerator,
            RandomGenerator borderGenerator,
            AuxiliaryGenerator auxGenerator) {
        super(region);
        _safeGenerator = safeGenerator;
        _borderGenerator = borderGenerator;
        _auxGenerator = auxGenerator;
    }

    public HyperCircularDataShape(Region region,
            RandomGenerator safeGenerator,
            RandomGenerator borderGenerator) {
        this(region, safeGenerator, borderGenerator, new AuxiliaryGenerator());
    }

    @Override
    public Point generateSafePoint() {
        Point point = null;
        do {
//          Implemented approach is based on "An Efficient Method for Generating Points Uniformly Distributed
//          in Hyperellipsoids". As in Cholesky decomposition we only use diagnoal matrices, regular calculations
//          can be skipped.
            List<Double> coord = _safeGenerator.getNumbers();
            double s = Math.sqrt(coord.stream().reduce(0.0, (ss, x) -> ss + x*x));
            Distribution d = getRegion().getDistribution();
            double rr = d.getType() == DistributionType.UNIFORM ? _auxGenerator.getUniformNumber() : _auxGenerator.getGaussianNumber(d.getNumStDevs());
            double r = Math.pow(rr, 1.0/coord.size());

            for (int i = 0; i < _region.getCenter().getNumDimensions(); ++i) {
                coord.set(i, r/s*coord.get(i)*_region.getSafeRadius().get(i) + _region.getCenter().get(i));
            }
            point = new Point(coord);
//      This check below is no loger necessary -- I have left it only for sanity. The same applies to the check
//      in the generateBorderPoint method.
        } while (!isCovered(point, RangeType.SAFE));
        return point;
    }

    @Override
    public Point generateBorderPoint() {
        Point point = null;
        do {
            List<Double> coord = _borderGenerator.getNumbers();
            double s = Math.sqrt(coord.stream().reduce(0.0, (ss, x) -> ss + x*x));
            double r = Math.pow(_auxGenerator.getUniformNumber(), 1.0/coord.size());
            for (int i = 0; i < _region.getCenter().getNumDimensions(); ++i) {
                coord.set(i, r/s*coord.get(i)*_region.getBorderRadius().get(i) + _region.getCenter().get(i));
            }
            point = new Point(coord);
        } while (isCovered(point, RangeType.SAFE) || !isCovered(point, RangeType.BORDER));
        return point;
    }

    @Override
    protected boolean isCovered(Point point, Size radius) {
        Validate.isTrue(point.getNumDimensions() == radius.getNumDimensions());
        double distance = 0;
        for (int i = 0; i < point.getNumDimensions(); ++i) {
            distance += Math.pow(point.get(i) - _region.getCenter().get(i), 2)
                    / Math.pow(radius.get(i), 2);
        }
        return distance <= 1;
    }

}

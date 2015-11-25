package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Coordinate;
import pl.poznan.put.cs.idss.generator.settings.Rotation;

public class Rotator {

    private Point middle;
    private final double sinus;
    private final double cosinus;
    private int firstAxis;
    private int secondAxis;
    private double backwardCosinus;
    private double backwardSinus;

    public Rotator(Coordinate coord, Rotation rotation) {
        this(new Point(coord),
                rotation.getAngle(), rotation.getAxis1(), rotation.getAxis2());
    }

    public Rotator(Point middle, double angle, int firstAxis, int secondAxis) {
        this.middle = middle;
        double radian = getRadian(angle);
        this.sinus = Math.sin(radian);
        this.backwardSinus = Math.sin(-radian);
        this.cosinus = Math.cos(radian);
        this.backwardCosinus = Math.cos(-radian);
        this.firstAxis = firstAxis;
        this.secondAxis = secondAxis;
    }

    public final double getRadian(double angle) {
        return angle / 180.0 * Math.PI;
    }

    public Point rotate(Point point) {
        return rotate(point, sinus, cosinus);
    }

    private Point rotate(Point point, double sinus, double cosinus) {

        Coordinate rotated = new Coordinate(point.getCoordinate());

        double x = rotated.get(firstAxis) - middle.getValue(firstAxis);
        double y = rotated.get(secondAxis) - middle.getValue(secondAxis);
        double rotatedX = x * cosinus - y * sinus;
        double rotatedY = x * sinus + y * cosinus;

        rotated.set(firstAxis, rotatedX + middle.getValue(firstAxis));
        rotated.set(secondAxis, rotatedY + middle.getValue(secondAxis));
        return new Point(rotated);
    }

    public Point restore(Point rotated) {
        return rotate(rotated, backwardSinus, backwardCosinus);
    }

}

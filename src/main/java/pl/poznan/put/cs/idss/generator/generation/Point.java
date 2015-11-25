package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Coordinate;
import java.util.List;
import lombok.Getter;

public class Point {

    @Getter
    private final Coordinate _coordinate;

    public Point(List<Double> list) {
        _coordinate = new Coordinate(list);
    }
    
    public Point(Coordinate coord) {
        _coordinate = new Coordinate(coord);
    }

    @Override
    public String toString() {
        return _coordinate.toString();
    }

    public int size() {
        return _coordinate.size();
    }
    
    public int getNumDimensions() {
        return size();
    }
    
    public int getNumberOfDimensions() {
        return size();
    }

    public Point add(double difference) {
        Coordinate result = new Coordinate(_coordinate);
        for (int i = 0; i < result.size(); ++i) {
            result.set(i, result.get(i) + difference);
        }
        return new Point(result);
    }

    public double getValue(int index) {
        return get(index);
    }
    
    public double get(int index) {
        return _coordinate.get(index);
    }

    public boolean isLowerOrEqual(Point other) {
        checkDimensionality(other);

        for (int i = 0; i < _coordinate.size(); ++i) {
            if (!(_coordinate.get(i) <= other._coordinate.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Point)) {
            return false;
        }

        Point other = (Point) object;
        checkDimensionality(other);

        for (int i = 0; i < _coordinate.size(); ++i) {
            if (Math.abs(_coordinate.get(i) - other._coordinate.get(i)) >= 0.00000001) {
                return false;
            }
        }
        return true;
    }

    private void checkDimensionality(Point other) {
        if (other._coordinate.size() != _coordinate.size()) {
            throw new UnsupportedOperationException("Number of dimensions must be the same!"
                    + "[this]=" + toString() + " and"
                    + "[other] " + other.toString());
        }
    }

    public double distance(Point other) {
        checkDimensionality(other);
        double sum = 0;
        for (int i = 0; i < getNumberOfDimensions(); ++i) {
            sum += Math.pow(getValue(i) - other.getValue(i), 2);
        }
        return Math.sqrt(sum);
    }

}

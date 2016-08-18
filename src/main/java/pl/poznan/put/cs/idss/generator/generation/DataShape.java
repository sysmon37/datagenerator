package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Region;
import lombok.Getter;
import pl.poznan.put.cs.idss.generator.settings.RangeType;
import pl.poznan.put.cs.idss.generator.settings.Size;

public abstract class DataShape {
    
    @Getter
    protected final Region _region;
    
    public DataShape(Region region) {
        _region = region;
    }
    
    public abstract Point generateSafePoint();

    public abstract Point generateBorderPoint();


    protected abstract boolean isCovered(Point point, Size radius);
    
    protected boolean isCovered(Point point, RangeType range) {
        return isCovered(point, _region.getRadius(range));
    }
    
        
    public boolean isInNoOutlierRange(Point point) {
        return isCovered(point, RangeType.NO_OUTLIER);
    }

    public boolean isInSafeRange(Point point) {
        return isCovered(point, RangeType.SAFE);
    }

    /**
     * Checks whether a given point is in safe + borderline zone (this is for consistency 
     * with the initial version).
     * @param point point being checked
     * @return 
     */
    public boolean isCovered(Point point) {
        return isCovered(point, RangeType.BORDER);
    }

}

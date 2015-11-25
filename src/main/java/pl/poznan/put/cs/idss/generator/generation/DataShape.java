package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.Region;
import lombok.Getter;

public abstract class DataShape {
    
    @Getter
    protected final Region _region;
    
    public DataShape(Region region) {
        _region = region;
    }
    
    public abstract Point generateCorePoint();

    public abstract Point generateOverlappingPoint();


    protected abstract boolean isCovered(Point point, double margin);
        
    public boolean isInNoOutlierZone(Point point) {
        return isCovered(point, _region.getBorderZone() + _region.getNoOutlierZone());
    }

    public boolean isInCoreZone(Point point) {
        return isCovered(point, 0);
    }

    /**
     * Checks whether a given point is in safe + borderline zone (this is for consistency 
     * with the initial version).
     * @param point point being checked
     * @return 
     */
    public boolean isCovered(Point point) {
        return isCovered(point, _region.getBorderZone());
    }

}

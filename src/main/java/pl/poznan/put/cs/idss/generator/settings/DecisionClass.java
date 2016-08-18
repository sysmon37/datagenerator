package pl.poznan.put.cs.idss.generator.settings;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Configuration for a certain decision class
 * @author swilk
 */
@Data
public class DecisionClass {
    private List<Region> _regions = new ArrayList<>();
    private Ratio _exampleTypeRatio = null;
    private Ratio _regionRatio = null;
    
    private Ratio[] _exampleTypeDistributions = new Ratio[] {new Ratio(Ratio.SIZE_EXAMPLE_TYPE, 0.0), new Ratio(Ratio.SIZE_EXAMPLE_TYPE, 0.0)};
    private Ratio[] _regionDistributions = new Ratio[] {new Ratio(1, 1.0), new Ratio(1, 1.0)};;
    

    /**
     * Returns the number of examples from a given set (learn or test).
     * 
     * @param setIndex -- @see Ratio#LEARN and @see Ratio#TEST
     * @return 
     */
    public int getNumExamples(int setIndex) {
        return (int) _exampleTypeDistributions[setIndex].getTotal();
    }
    
    /**
     * Returns the number of objects from a given set (learn or test) and of given
     * type (safe, border, rare, outlier).
     * 
     * @param setIndex -- @see Ratio#LEARN or @see Ratio#TEST
     * @param typeIndex -- @see Ratio#SAFE or @see Ratio#BORDER or @see Ratio#RARE or @see Ratio#OUTLIER
     * @return 
     */
    public int getNumExamples(int setIndex, int typeIndex) {
        return (int) _exampleTypeDistributions[setIndex].get(typeIndex);
    }
    
    public int getNumRegions() {
        return _regions.size();
    }
    
    public Region getRegion(int regionIndex) {
        return _regions.get(regionIndex);
    }
    
    public DecisionClass setExampleTypeDistribution(int setIndex, Ratio distribution) {
        _exampleTypeDistributions[setIndex] = new Ratio(distribution);
        return this;
    }
    
    public Ratio getExampleTypeDistribution(int setIndex) {
        return _exampleTypeDistributions[setIndex];
    }

    public DecisionClass setRegionDistribution(int setIndex, Ratio distribution) {
        _regionDistributions[setIndex] = new Ratio(distribution);
        return this;
    }
    
    public Ratio getRegionDistribution(int setIndex) {
        return _regionDistributions[setIndex];
    }
    
    public void updateRegionsRadiuses() {
        _regions.stream().forEach((r) -> r.updateRadiuses());
    }
    
    public DecisionClass setRegions(List<Region> regions) {
        _regions = regions;
        _regions.stream().forEach((r) -> r.setDecisionClass(this));
        return this;
    }
    
}

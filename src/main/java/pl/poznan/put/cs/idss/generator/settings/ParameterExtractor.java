package pl.poznan.put.cs.idss.generator.settings;

import java.util.ArrayList;
import java.util.List;

import pl.poznan.put.cs.idss.generator.factories.OutlierDescription;
import pl.poznan.put.cs.idss.generator.factories.RegionDescription;
import pl.poznan.put.cs.idss.generator.generation.OutlierType;
import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.Validate;

/**
 * This class acts now as a bridge between the new GeneratorSettings and 
 * the remaining code.
 */
@ToString
public class ParameterExtractor {

    @Getter @Setter private boolean _addLabels  = false;
    @Getter @Setter private int _labeledClassIndex = 0;
    
    @Getter
    private GeneratorSettings _settings = null;
    
    public ParameterExtractor(GeneratorSettings settings) {
        Validate.notNull(settings);
        _settings = settings;
    }
    
    List<String> values = new ArrayList<>();

    public void add(List<String> values) {
        this.values.addAll(values);
    }

    public int getTotalNumberOfExamples() {
        return _settings.getNumExamples();
    }

    public int getNumberOfAttributes() {
        return _settings.getNumAttributes();
    }

    public int getNumberOfClasses() {
        return _settings.getNumClasses();
    }

    public List<RegionDescription> getRegionDescriptions() {      
        List<RegionDescription> descriptions = new ArrayList<>();
        for (int c = 0; c < _settings.getNumClasses(); c++) {
            Class clazz = _settings.getClass(c);
            for (int r = 0; r < clazz.getNumRegions(); r++)
                descriptions.add(new RegionDescription(clazz.getRegion(r), c));
        }
        return descriptions;
    }

    /**
     * Creates an array of rare/outlier descriptions. Each entry gives an absolute 
     * number of cases of specific type (rare, outlier) and belonging to a specific 
     * class.
     * 
     * @return 
     */
    public List<OutlierDescription> getOutlierDescriptions() {
        List<OutlierDescription> outlierDescriptions = new ArrayList<>();

        for (int c = 0; c < _settings.getNumClasses(); c++) {
            Class clazz = _settings.getClass(c);
            Ratio exampleTypeDistribution = clazz.getExampleTypeDistribution(Ratio.LEARN);
            for (int t : Arrays.asList(Ratio.OUTLIER, Ratio.RARE)) {
                // group is a single outlier or an "island of NUM_RARE_PER_GROUP rare cases
                int numGroups = (int) exampleTypeDistribution.get(t);
                if (t == Ratio.RARE)
                    numGroups /= GeneratorSettings.NUM_RARE_PER_GROUP;
                for (int g = 0; g < numGroups; g++)
                    outlierDescriptions.add(new OutlierDescription(t == Ratio.OUTLIER ? OutlierType.OUTLIER : OutlierType.RARE_CASE, c));
            }
        }
        return outlierDescriptions;
    }

    public int getNumberOfTrainingTestPairsToBeGenerated() {
        return _settings.getNumLearnTestPairs();
    }
    
    public int getNumLearnTestPairs() {
        return _settings.getNumLearnTestPairs();
    }

    public double getMinOutlierDistance() {
        return _settings.getMinOutlierDistance();
    }
    
}

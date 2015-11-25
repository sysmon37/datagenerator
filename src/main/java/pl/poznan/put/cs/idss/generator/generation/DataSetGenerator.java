package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
import java.util.List;
import pl.poznan.put.cs.idss.generator.settings.Ratio;

public class DataSetGenerator {

    private final List<RegionGenerator> _regionGenerators;
    private final OutlierGenerator _outliersGenerator;

    public DataSetGenerator(List<RegionGenerator> regionGenerators, OutlierGenerator outlierGenerator) {
        _regionGenerators = regionGenerators;
        _outliersGenerator = outlierGenerator;
    }

    public List<Example> generateExamples(int setIndex) {
        List<Example> examples = new ArrayList<>();
        for (RegionGenerator regionGenerator : _regionGenerators) {
            examples.addAll(regionGenerator.generateExamples(setIndex));
        }
        examples.addAll(_outliersGenerator.generateExamples(setIndex, examples));

        return examples;
        
    }
    public List<Example> generateLearnExamples() {
        return generateExamples(Ratio.LEARN);
    }

    public List<Example> generateTestExamples() {
        return generateExamples(Ratio.TEST);
    }
}

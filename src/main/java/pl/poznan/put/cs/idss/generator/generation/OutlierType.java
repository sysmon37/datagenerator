package pl.poznan.put.cs.idss.generator.generation;

import pl.poznan.put.cs.idss.generator.settings.GeneratorSettings;

public enum OutlierType {
    OUTLIER(1), RARE_CASE(GeneratorSettings.NUM_RARE_PER_GROUP);

    private final int _numLearnExamplesPerGroup;

    OutlierType(int numLearnExamplesPerGroup) {
        this._numLearnExamplesPerGroup = numLearnExamplesPerGroup;
    }

    public int numLearnExamplesPerGroup() {
        return _numLearnExamplesPerGroup;
    }

    public int numTestExamplesPerGroup() {
        return 1;
    }

    public Example.Label toExampleLabel() {
        return this == OutlierType.OUTLIER ? Example.Label.OUTLIER : Example.Label.RARE;
    }
}

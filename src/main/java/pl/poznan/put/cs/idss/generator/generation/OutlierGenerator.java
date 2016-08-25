package pl.poznan.put.cs.idss.generator.generation;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

import pl.poznan.put.cs.idss.generator.factories.AdditionalPointGeneratorFactory;
import pl.poznan.put.cs.idss.generator.factories.OutlierDescription;
import pl.poznan.put.cs.idss.generator.settings.Coordinate;
import pl.poznan.put.cs.idss.generator.settings.Ratio;

public class OutlierGenerator {

    private List<OutlierDescription> outlierDescriptions;
    private OutlierFirstPointGenerator generator;
    private IsInsideForbiddenZoneChecker forbiddenZoneChecker;
    private OutlierDistanceBreachedChecker distanceChecker;
    private OutlierNeighbourhoodChecker neighbourhoodChecker;
    private final static int GENERATION_OUTLIER_TRIALS_NUMBER = 100000;
    private List<PointGeneratorAndExamples> pointGeneratorsAndTheirExamples;
    private List<Example> copyOfExistingExamples;
    private List<Example> outliers;
    private AdditionalPointGeneratorFactory additionalPointGeneratorFactory;
    private Coordinate minCoordinate;
    private Coordinate maxCoordinate;

    public OutlierGenerator(List<OutlierDescription> outlierDescriptions,
            OutlierFirstPointGenerator generator,
            IsInsideForbiddenZoneChecker forbiddenZoneChecker,
            OutlierDistanceBreachedChecker distanceChecker,
            OutlierNeighbourhoodChecker neighbourhoodChecker,
            AdditionalPointGeneratorFactory additionalPointGeneratorFactory,
            Coordinate minCoordinate,
            Coordinate maxCoordinate)
    {
        this.outlierDescriptions = outlierDescriptions;
        this.generator = generator;
        this.forbiddenZoneChecker = forbiddenZoneChecker;
        this.distanceChecker = distanceChecker;
        this.neighbourhoodChecker = neighbourhoodChecker;
        this.additionalPointGeneratorFactory = additionalPointGeneratorFactory;
        this.minCoordinate = minCoordinate;
        this.maxCoordinate = maxCoordinate;
    }

    public List<Example> generateExamples(int setIndex, List<Example> existingExamples) {
        if (setIndex == Ratio.LEARN)
            return generateLearnExamples(existingExamples);
        else
            return generateTestExamples();
    }
    
    public List<Example> generateLearnExamples(List<Example> existingExamples) {
        copyOfExistingExamples = new ArrayList<>(existingExamples);
        pointGeneratorsAndTheirExamples = new ArrayList<>();
        outliers = new ArrayList<>();
        for (OutlierDescription outlierDescription : outlierDescriptions) {
            Example.Label label = outlierDescription.type.toExampleLabel();

            List<Example> currentGroup = new ArrayList<>();

            Example firstExample = generate(currentGroup,
                    outlierDescription.classIndex,
                    generator);
            firstExample.setLabel(label);
            currentGroup.add(firstExample);
            outlierDescription.middle = firstExample.getPoint();

            AdditionalOutlierPointGenerator additionalPointGenerator = additionalPointGeneratorFactory.createPointGenerator(existingExamples, firstExample);
            while (currentGroup.size() < outlierDescription.numLearnExamples) {
                Example nextExample = generate(currentGroup,
                        outlierDescription.classIndex,
                        additionalPointGenerator);
                nextExample.setLabel(label);
                currentGroup.add(nextExample);
            }

            pointGeneratorsAndTheirExamples.add(new PointGeneratorAndExamples(additionalPointGenerator, currentGroup, outlierDescription));
            outliers.addAll(currentGroup);
            copyOfExistingExamples.addAll(currentGroup);
        }
        return outliers;
    }

    private Example generate(List<Example> currentGroup,
            int classIndex,
            PointGenerator generator) {
        for (int z = 1; z <= GENERATION_OUTLIER_TRIALS_NUMBER; ++z) {
            Example generatedExample = new Example(generator.generate(), classIndex);
            if (isValid(generatedExample, currentGroup)) {
                return generatedExample;
            }
        }
        throw new IllegalArgumentException("Cannot generate outlier!");
    }

    private boolean isValid(Example generatedExample,
            List<Example> currentGroup) {
        return (!forbiddenZoneChecker.isInsideForbiddenZone(generatedExample))
                && (!distanceChecker.isInterOutlierDistanceBreached(generatedExample, outliers, currentGroup))
                && (!neighbourhoodChecker.hasNeighbourFromClassNotBelongingToOutlier(generatedExample,
                        copyOfExistingExamples,
                        currentGroup))
                && isExampleInsideBoundaries(generatedExample);
    }

    private boolean isExampleInsideBoundaries(Example example)
    {
        for (int i = 0; i < example.getPoint().getNumDimensions(); ++i)
        {
            double value = example.getPoint().get(i);
            if (!(minCoordinate.get(i) <= value &&
                  value <= maxCoordinate.get(i)))
            {
                return false;
            }
        }
        return true;
    }

    public List<Example> generateTestExamples() {
        if (pointGeneratorsAndTheirExamples == null) {
            throw new IllegalArgumentException("Generating testing examples must be preceded by generating learning examples!");
        }
        List<Example> result = new ArrayList<>();
        for (PointGeneratorAndExamples pge : pointGeneratorsAndTheirExamples) {
            for (int i = 0; i <  pge.getOutlierDescription().numTestExamples; i++) {
                Example example = generate(pge.getExamples(),
                        pge.getOutlierDescription().classIndex,
                        pge.getGenerator());
                example.setLabel(pge.getOutlierDescription().type.toExampleLabel());
                result.add(example);
            }
        }
        return result;
    }
}

@Data
class PointGeneratorAndExamples {

    private PointGenerator _generator;
    private List<Example> _examples;
    private OutlierDescription _outlierDescription;  

    public PointGeneratorAndExamples(PointGenerator generator, List<Example> examples, OutlierDescription outlierDescription) {
        _generator = generator;
        _examples = examples;
        _outlierDescription = outlierDescription;
    }
}

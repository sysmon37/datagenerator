package cli;

import java.util.ArrayList;
import java.util.List;

import factories.OutlierDescription;
import factories.RegionDescription;
import factories.RotationDescription;
import generator.OutlierType;
import lombok.Getter;
import lombok.Setter;

public class ParameterExtractor {

    @Getter @Setter private boolean addLabels  = false;
    @Getter @Setter private String labeledClassIndex = "";
    
    
    List<String> values = new ArrayList<String>();

    public void add(List<String> values) {
        this.values.addAll(values);
    }

    public int getTotalNumberOfExamples() {
        return Integer.parseInt(values.get(0));
    }

    public int getNumberOfAttributes() {
        return Integer.parseInt(values.get(1));
    }

    public int getNumberOfClasses() {
        return Integer.parseInt(values.get(2));
    }

    public List<RegionDescription> getCommands() {
        List<RegionDescription> regions = parseRegionDescriptions();
        calculateAbsoluteNumbersOfExamples(regions);
        parseRotations(regions);
        return regions;
    }

    private void calculateAbsoluteNumbersOfExamples(List<RegionDescription> regions) {
        List<Integer> trainigExampleSizes = calculateAbsoluteNumbersPerRegion(regions,
                getAbsoluteDistributionOfRegionalExamplesForTraining());
        List<Integer> testingExampleSizes = calculateAbsoluteNumbersPerRegion(regions,
                getAbsoluteDistributionOfRegionalExamplesForTesting());
        for (int i = 0; i < regions.size(); ++i) {
            regions.get(i).numberOfTrainingExamples = trainigExampleSizes.get(i);
            regions.get(i).numberOfTestingExamples = testingExampleSizes.get(i);
        }
    }

    private int[] getAbsoluteDistributionOfRegionalExamplesForTesting() {
        return getAbsoluteDistributionOfRegionalExamples(getTestingExamplesSizeAsFractionOfTraining(), 1, 1);
    }

    public int[] getAbsoluteDistributionOfRegionalExamplesForTraining() {
        return getAbsoluteDistributionOfRegionalExamples(1,
                OutlierType.OUTLIER.getNumberOfTrainingExamples(),
                OutlierType.RARE_CASE.getNumberOfTrainingExamples());
    }

    private List<Integer> calculateAbsoluteNumbersPerRegion(List<RegionDescription> descriptions,
            int[] absoluteDistributionOfRegionExamples) {
        List<Integer> result = new ArrayList<Integer>();
        int[] sumOfClass = getInsideClassDenominators(descriptions);
        for (RegionDescription c : descriptions) {
            final int classIndex = Integer.parseInt(c.classIndex);
            int value = (int) Math.round(c.imbalancedRatio * absoluteDistributionOfRegionExamples[classIndex] / sumOfClass[classIndex]);
            sumOfClass[classIndex] -= c.imbalancedRatio;
            absoluteDistributionOfRegionExamples[classIndex] -= value;
            result.add(value);
        }
        return result;
    }

    private List<RegionDescription> parseRegionDescriptions() {
        List<RegionDescription> result = new ArrayList<RegionDescription>();
        int descriptionBeginning = getOffsetToRegionDescription();
        int[] regionsDistribution = getNumberOfRegionsPerClass();
        List<Integer> classAssignments = getClassAssignments(regionsDistribution);

        if (classAssignments.size() * getNumberOfParametersPerCommand() + descriptionBeginning > values.size()) {
            throw new IllegalArgumentException("Region description is broken! Too few parameters!");
        }

        for (int i = 0; i < classAssignments.size(); ++i) {
            result.add(new RegionDescription(values.subList(descriptionBeginning, descriptionBeginning + getNumberOfParametersPerCommand()),
                    Integer.toString(classAssignments.get(i))));
            descriptionBeginning += getNumberOfParametersPerCommand();
        }
        return result;
    }

    private List<Integer> getClassAssignments(int[] classDistribution) {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < classDistribution.length; ++i) {
            for (int j = 0; j < classDistribution[i]; ++j) {
                result.add(i);
            }
        }
        return result;
    }

    private int[] getInsideClassDenominators(List<RegionDescription> commands) {
        int[] result = new int[getNumberOfClasses()];
        for (RegionDescription c : commands) {
            result[Integer.parseInt(c.classIndex)] += c.imbalancedRatio;
        }
        return result;
    }

    private double[] getExamplesPerClassDistribution() {
        int[] imbalancedDistribution = loadClassDistribution(getOffsetToClassImbalancedRatio());
        int sum = 0;
        for (int i = 0; i < getNumberOfClasses(); ++i) {
            sum += imbalancedDistribution[i];
        }

        double[] result = new double[getNumberOfClasses()];
        for (int i = 0; i < getNumberOfClasses(); ++i) {
            result[i] = (double) imbalancedDistribution[i] / sum;
        }
        return result;
    }

    private int[] getAbsoluteDistributionOfRegionalExamples(double fractionOfTotalNumberOfExamples,
            int examplesPerOutlierIsland,
            int examplesPerRareCaseIsland) {
        double[] examplesPerClassDistribution = getExamplesPerClassDistribution();
        double numberOfExamples = getTotalNumberOfExamples() * fractionOfTotalNumberOfExamples;
        int[] result = new int[getNumberOfClasses()];
        double denominator = 1;
        for (int i = 0; i < getNumberOfClasses(); ++i) {
            result[i] = (int) Math.round(examplesPerClassDistribution[i] / denominator * numberOfExamples);
            denominator -= examplesPerClassDistribution[i];
            numberOfExamples -= result[i];
        }

        for (int i = 0; i < getNumberOfClasses(); ++i) {
            result[i] -= getNumberOfExamplesBelongingToIsland(i,
                    examplesPerOutlierIsland,
                    examplesPerRareCaseIsland);
        }
        return result;
    }

    private int getNumberOfExamplesBelongingToIsland(int classIndex,
            int examplesPerOutlierIsland,
            int examplesPerRareCaseIsland) {
        return loadClassDistribution(getOffsetToOutliersDescription())[classIndex] * examplesPerOutlierIsland
                + loadClassDistribution(getOffsetToOutliersDescription() + getNumberOfClasses())[classIndex] * examplesPerRareCaseIsland;
    }

    public int[] getNumberOfRegionsPerClass() {
        return loadClassDistribution(getOffsetToNumberOfRegionsPerClass());
    }

    private int[] loadClassDistribution(final int offest) {
        int[] result = new int[getNumberOfClasses()];
        for (int i = 0; i < getNumberOfClasses(); ++i) {
            result[i] = Integer.parseInt(values.get(offest + i));
        }
        return result;
    }

    private void parseRotations(List<RegionDescription> commands) {
        for (int i = getOffsetToRotationsDescription(); i + 4 <= values.size(); i += 4) {
            int fieldNumber = Integer.parseInt(values.get(i)) - 1;
            commands.get(fieldNumber).rotations.add(new RotationDescription(
                    Integer.parseInt(values.get(i + 1)) - 1,
                    Integer.parseInt(values.get(i + 2)) - 1,
                    Double.parseDouble(values.get(i + 3))));
        }
    }

    public double getInterOutlierDistance() {
        return Double.parseDouble(values.get(getOffsetToInterOutlierDistance()));
    }

    public int getOffsetToInterOutlierDistance() {
        return getOffsetToOutliersDescription()
                + getNumberOfClasses()
                + getNumberOfClasses();
    }

    private int getOffsetToRotationsDescription() {
        return getOffsetToInterOutlierDistance()
                + 1
                + getOutliers().size() * getNumberOfParametersPerOutlier();
    }

    private int getNumberOfParametersPerOutlier() {
        return 0;
    }

    private int getNumberOfParametersPerCommand() {
        return 6 + 2 * getNumberOfAttributes();
    }

    private int getOffsetToOutliersDescription() {
        return getOffsetToRegionDescription() + parseRegionDescriptions().size() * getNumberOfParametersPerCommand();
    }

    private int getOffsetToClassImbalancedRatio() {
        return 5;
    }

    private int getOffsetToNumberOfRegionsPerClass() {
        return getOffsetToClassImbalancedRatio() + getNumberOfClasses();
    }

    private int getOffsetToRegionDescription() {
        return getOffsetToNumberOfRegionsPerClass() + getNumberOfClasses();
    }

    public List<OutlierDescription> getOutliers() {
        List<OutlierDescription> outlierDescriptions = new ArrayList<OutlierDescription>();

        List<Integer> outliersClassAssignments = getClassAssignments(loadClassDistribution(getOffsetToOutliersDescription()));
        for (Integer assignment : outliersClassAssignments) {
            outlierDescriptions.add(new OutlierDescription(OutlierType.OUTLIER, assignment.toString()));
        }

        List<Integer> rareCasesClassAssignments = getClassAssignments(loadClassDistribution(getOffsetToOutliersDescription() + getNumberOfClasses()));
        for (Integer assignment : rareCasesClassAssignments) {
            outlierDescriptions.add(new OutlierDescription(OutlierType.RARE_CASE, assignment.toString()));
        }

        return outlierDescriptions;
    }

    public double getTestingExamplesSizeAsFractionOfTraining() {
        return Double.parseDouble(values.get(3));
    }

    public int getNumberOfTrainingTestPairsToBeGenerated() {
        return Integer.parseInt(values.get(4));
    }
}

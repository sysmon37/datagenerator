package pl.poznan.put.cs.idss.generator.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.DoubleValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import pl.poznan.put.cs.idss.generator.generation.OutlierType;

/**
 * Keeps all configuration settings extracted from a configuration file and from
 * a command line
 *
 * @author swilk
 */
@Getter
@Slf4j
public class GeneratorSettings {

    private final static String KEY_CLASSES = "classes";
    private final static String KEY_ATTRIBUTES = "attributes";
    private final static String KEY_DECISION = "decision";
    private final static String KEY_EXAMPLES = "examples";
    private final static String KEY_REGIONS = "regions";
    private final static String KEY_NAMES = "names";
    private final static String KEY_CLASS = "class";
    private final static String KEY_DEFAULT_REGION = "defaultRegion";
    private final static String KEY_DEFAULT_CLASS = "defaultClass";

    private final static String KEY_REGION = "region";
    private final static String KEY_WEIGHT = "weight";
    private final static String KEY_SHAPE = "shape";
    private final static String KEY_BORDER_ZONE = "borderZone";
    private final static String KEY_NO_OUTLIER_ZONE = "noOutlierZone";
    private final static String KEY_CENTER = "center";
    private final static String KEY_RADIUS = "radius";
    private final static String KEY_ROTATION = "rotation";
    private final static String KEY_DISTRIBUTION = "distribution";
    private final static String KEY_MIN_OUTLIER_DISTANCE = "minOutlierDistance";
    private final static String KEY_EXAMPLE_TYPE_RATIO = "exampleTypeRatio";
    private final static String KEY_CLASS_RATIO = "classRatio";
    private final static String KEY_FILE_NAME = "fileName";
    private final static String KEY_LEARN = "learn";
    private final static String KEY_TEST = "test";
    private final static String KEY_LEARN_TEST_RATIO = "learnTestRatio";
    private final static String KEY_LEARN_TEST_PAIRS = "learnTestPairs";
    private final static String KEY_EXAMPLE_TYPE_LABELS = "exampleTypeLabels";
    private final static String KEY_WRITER = "writer";

    private final static String KEY_BORDER = "border";
    private final static String VALUE_ALL = "*";

    private static final String NAME_DECISION = "D";
    private static final String NAME_CLASSS = "%d";
    private static final String NAME_ATTRIBUTTE = "X%d";


    public final static int NUM_RARE_PER_GROUP = 2;
    
    
    private List<DecisionClass> _decisionClasses = null;
    private Ratio _classRatio = null;

    private double _minOutlierDistance = 0;
    
    private int _numExamples = 0;
    private int _numAttributes = 0;
    
    private int _numLearnTestPairs = 0;
    private Ratio _learnTestRatio = null;
    
    private String _learnFileName;
    private String _testFileName;
    
    private List<String> _attributeNames = null;
    private List<String> _classNames = null;
    private String _decisionName;
    
    private Ratio _learnTestDistribution = null;
    private final Ratio[] _classDistributions = new Ratio[Ratio.SIZE_LEARN_TEST];
    
    private List<Integer> _labeledClassIndexes = null;
    
    private String _writer;
    
    public int getNumClasses() {
        return _decisionClasses.size();
    }

    public void read(String fileName, Properties cmdLineProperties) throws ConfigurationException {
        CompositeConfiguration config = new CompositeConfiguration();

        config.addConfiguration(new MapConfiguration(cmdLineProperties));
        config.addConfiguration(new PropertiesConfiguration(fileName));

        read(config);
    }
    
    public void read(Properties cmdLineProperties) throws ConfigurationException {
        read(new MapConfiguration(cmdLineProperties));
    }
    
    public void read(Configuration config) throws ConfigurationException {
        try { 
            doRead(config);
        }
        catch (IllegalStateException | IllegalArgumentException x) {
            throw new ConfigurationException(x);
        }
    }

    protected void doRead(Configuration config) throws ConfigurationException, IllegalArgumentException {
        
        // Read information about attributes (currently limited to their number)
//        _numAttributes = extractInteger(config, KEY_ATTRIBUTES, true, (n) -> n > 0 && n <= 40);
        // As we have discarded LDS, there is no upper limit for the number of features.
        _numAttributes = extractInteger(config, KEY_ATTRIBUTES, true, (n) -> n > 0);
        
        // Read information about classes
        readClasses(config);
        
        // Read information about names
        String key = String.format("%s.%s", KEY_NAMES, KEY_ATTRIBUTES);
        _attributeNames = extractStringList(config, key, false, getNumAttributes(), (s) -> !s.isEmpty());
        if (_attributeNames.isEmpty()) {
            for (int a = 0; a < getNumAttributes(); a++)
                _attributeNames.add(String.format(NAME_ATTRIBUTTE, a + 1));
        }
        
        key = String.format("%s.%s", KEY_NAMES, KEY_CLASSES);
        _classNames = extractStringList(config, key, false, getNumClasses(), (s) -> !s.isEmpty());
        if (_classNames.isEmpty()) {
            for (int c = 0; c < getNumClasses(); c++)
                _classNames.add(String.format(NAME_CLASSS, c + 1));
        }
        
        key = String.format("%s.%s", KEY_NAMES, KEY_DECISION);
        _decisionName = extractString(config, key, false, (s) -> !s.isEmpty());
        if (_decisionName == null)
            _decisionName = NAME_DECISION;
        
        // Read min outlier distance
        _minOutlierDistance = extractDouble(config, KEY_MIN_OUTLIER_DISTANCE, true, (d) -> d >= 0);
        
        // Default region
        Region defaultRegion = extractRegion(config, KEY_DEFAULT_REGION, null);
        
        // Specific regions
        for (int c = 0; c < getNumClasses(); c++) {
            key = String.format("%s.%d.%s", KEY_CLASS, c + 1, KEY_REGIONS);
            int numRegions = extractInteger(config, key, true, (n) -> n > 0);
            List<Region> regions = new ArrayList<>(numRegions);
            List<Double> regionRatioData = new ArrayList<>(numRegions);
            for (int r = 0; r < numRegions; r++) { 
                key = String.format("%s.%d.%s.%d", KEY_CLASS, c + 1, KEY_REGION, r + 1);
                Region region = extractRegion(config, key, defaultRegion);
                regions.add(region);
                regionRatioData.add(region.getWeight());
            }
            _decisionClasses.get(c).setRegions(regions).setRegionRatio(new Ratio(regionRatioData));
        }
        
        // Examples
        _numExamples = extractInteger(config, KEY_EXAMPLES, true, (n) -> n > 0);
        
        // Learn-test ratio
        _learnTestRatio = extractRatio(config, KEY_LEARN_TEST_RATIO, false, Ratio.SIZE_LEARN_TEST);           
        if (_learnTestRatio == null)
            _learnTestRatio = new Ratio(1.0,0.0);
        Validate.isTrue(_learnTestRatio.get(Ratio.LEARN) > 0.0, "%s: no learn examples", KEY_LEARN_TEST_RATIO);

        // Learn-test pairs
        Integer tempNumLearnTestPairs = extractInteger(config, KEY_LEARN_TEST_PAIRS, false, (n) -> n >= 1);
        _numLearnTestPairs = tempNumLearnTestPairs == null ? 0 : tempNumLearnTestPairs;
            
        if (_numLearnTestPairs > 0) {
            Validate.isTrue(_learnTestRatio.get(Ratio.TEST) > 0.0, "%s, %s: Invalid combination of values (no test examples)", KEY_LEARN_TEST_RATIO, KEY_LEARN_TEST_PAIRS);
            key = String.format("%s.%s", KEY_FILE_NAME, KEY_LEARN);
            _learnFileName = extractString(config, key, true, (s) -> !s.isEmpty());
            key = String.format("%s.%s", KEY_FILE_NAME, KEY_TEST);
            _testFileName = extractString(config, key, true, (s) -> !s.isEmpty());            
        } else {
            _learnFileName = extractString(config, KEY_FILE_NAME, true, (s) -> !s.isEmpty());     
            _testFileName = StringUtils.EMPTY;
        }
        
        // Check whether any classes should be labeled with example types
        key = String.format("%s.%s", KEY_EXAMPLE_TYPE_LABELS, KEY_CLASSES);
        String tempLabelsString = extractString(config, key, false, (s) -> !s.isEmpty());
        if (tempLabelsString == null) {
            // empty list
            _labeledClassIndexes = new ArrayList<>();
        } else if (tempLabelsString.equalsIgnoreCase(VALUE_ALL)) {
            _labeledClassIndexes = new ArrayList<>(getNumClasses());
            for (int c = 0; c < getNumClasses(); c++)
                _labeledClassIndexes.add(c);
        } else {
            _labeledClassIndexes = extractList(config, key, true, 
                    (n) -> n > 0 && n <= getNumClasses(), 
                    (s) -> IntegerValidator.getInstance().validate(s, Locale.US), 
                    (i) -> i > 0 && i <= getNumClasses());
            // correction of class indexes -- internally we use 0-based indexes
            for (int i = 0; i < _labeledClassIndexes.size(); i++)
                _labeledClassIndexes.set(i, _labeledClassIndexes.get(i) - 1);
        }
        
        // Read information about the output format
        _writer = extractString(config, KEY_WRITER, false, (n) -> n == null || n.equalsIgnoreCase("CSV") || n.equalsIgnoreCase("ARFF"));
        if (_writer == null){
            _writer = "ARFF";
        } else {
            _writer = _writer.toUpperCase();
        }
                
        log.debug(toString());
        
        // Validate current configuration
        validate();
        
        // Update radiuses for regions from all decision classes
        _decisionClasses.stream().forEach((c) -> c.updateRegionsRadiuses());
        // 
        // Distribute examples into train and test sets, classes, types and regions
        distributeExamples();
    }

    private void readClasses(Configuration config) throws ConfigurationException {
        String key = KEY_CLASSES;
        int numClasses = extractInteger(config, key, true, (n) -> n > 0);
        _classRatio = extractRatio(config, KEY_CLASS_RATIO, true, numClasses);
        
        key = String.format("%s.%s", KEY_DEFAULT_CLASS, KEY_EXAMPLE_TYPE_RATIO);
        Ratio defaultExampleTypeRatio = extractRatio(config, key, false, Ratio.SIZE_EXAMPLE_TYPE);
        if (defaultExampleTypeRatio == null)
            defaultExampleTypeRatio = new Ratio(Arrays.asList(100.0, 0.0, 0.0, 0.0));
        
        _decisionClasses = new ArrayList<>();
        for (int c = 0; c < numClasses; c++) {
            DecisionClass clazz = new DecisionClass(); 
            key = String.format("%s.%d.%s", KEY_CLASS, c + 1, KEY_EXAMPLE_TYPE_RATIO);            
            Ratio exampleTypeRatio = extractRatio(config, key, false, Ratio.SIZE_EXAMPLE_TYPE);
            if (exampleTypeRatio == null)
                exampleTypeRatio = defaultExampleTypeRatio;
            clazz.setExampleTypeRatio(exampleTypeRatio);
            _decisionClasses.add(clazz);            
        }
    }

    private Region extractRegion(Configuration config, String baseKey, Region defaultRegion) throws ConfigurationException {

        boolean isDefault = defaultRegion == null;
        Region region = isDefault ? new Region() : new Region(defaultRegion);

        String key = String.format("%s.%s", baseKey, KEY_WEIGHT);
        Double weight = extractDouble(config, key, 
                !isDefault && region.getWeight() == null, (d) -> d > 0);
        if (weight != null) 
            region.setWeight(weight);
        else if (isDefault)
            region.setWeight(1.0);
        
        key = String.format("%s.%s", baseKey, KEY_SHAPE);
        String shapeString = extractString(config, key, 
                !isDefault && region.getShape() == null, (s) -> ShapeType.isValid(s));
        ShapeType shape = ShapeType.validate(shapeString);        
        if (shape != null) 
            region.setShape(shape);

        key = String.format("%s.%s", baseKey, KEY_CENTER);
        List<Double> values = extractDoubleList(config, key, 
                !isDefault && region.getCenter() == null, getNumAttributes(), (d) -> true);
        if (values.size() > 0) {
            Coordinate center = new Coordinate(values);
            region.setCenter(center);
        }
        
        key = String.format("%s.%s", baseKey, KEY_RADIUS);
        values = extractDoubleList(config, key, 
                !isDefault && region.getRadius() == null, getNumAttributes(), (d) -> d > 0);
        if (values.size() > 0) {
            Size radius = new Size(values);
            region.setRadius(radius);
        }
        
        key = String.format("%s.%s", baseKey, KEY_BORDER);
        String borderString = extractString(config, key, false, (s) -> BorderType.isValid(s));
        BorderType border = BorderType.validate(borderString);
        if (border != null)
            region.setBorder(border);
        else if (isDefault)
            region.setBorder(BorderType.FIXED);
        
        key = String.format("%s.%s", baseKey, KEY_BORDER_ZONE);
        Double borderZone = extractDouble(config, key, !isDefault && region.getBorder() == BorderType.FIXED && region.getBorderZone() == null, (d) -> d >= 0);
        if (borderZone != null && region.getShape() != ShapeType.INTEGUMENTAL) 
            region.setBorderZone(borderZone);
        else if (isDefault || region.getShape() == ShapeType.INTEGUMENTAL)
            region.setBorderZone(0.0);

        key = String.format("%s.%s", baseKey, KEY_NO_OUTLIER_ZONE);
        Double noOutlierZone = extractDouble(config, key, 
                !isDefault && region.getNoOutlierZone() == null, (d) -> d >= 0);
        if (noOutlierZone != null)
            region.setNoOutlierZone(noOutlierZone);
        
        key = String.format("%s.%s", baseKey, KEY_DISTRIBUTION);
        List<Object> distData = extractList(config, key, 
                !isDefault && region.getDistribution() == null, 
                (n) -> n >= 1 && n <= 2, 
                (s, i) -> {
                    if (i == 0) {
                        s = s.toUpperCase();
                        return DistributionType.isValid(s) ? s : null;
                    } else 
                        return DoubleValidator.getInstance().validate(s, Locale.US);
                } ,
                (o, i) -> i == 0 ? true : (Double) o > 0);
        if (!distData.isEmpty()) {
            Distribution distribution = new Distribution(DistributionType.validate((String) distData.get(0)));
            if (distData.size() > 1)
                distribution.setNumStDevs((Double) distData.get(1));
            region.setDistribution(distribution);
        } else if (isDefault)
            region.setDistribution(new Distribution(DistributionType.UNIFORM));
        
        key = String.format("%s.%s", baseKey, KEY_ROTATION);
        List<Object> rotationsData = extractList(config, key, 
                false,
                (n) -> n % 3 == 0,
                (s, i) -> {
                    if ((i + 1) % 3 == 0)
                        return DoubleValidator.getInstance().validate(s, Locale.US);
                    else
                        return IntegerValidator.getInstance().validate(s, Locale.US);                    
                },
                (o, i) -> {
                    if ((i + 1) % 3 == 0)
                        return true;
                    else
                        return IntegerValidator.getInstance().isInRange((Integer) o, 1, getNumAttributes());                    
                });
        List<Rotation> rotations = new ArrayList<>();                
        if (rotationsData.size() > 0) {
            for (int i = 0; i < rotationsData.size(); i += 3) {
                // correction of axes -- internally we use 0-based indexes
                int axis1 = (Integer) rotationsData.get(i) - 1;
                int axis2 = (Integer) rotationsData.get(i + 1) - 1;
                Validate.isTrue(axis1 != axis2, "%s: Not unique axes in rotations", key);
                double angle = (Double) rotationsData.get(i + 2);
                rotations.add(new Rotation(axis1, axis2, angle));
            }
        }
        region.setRotations(rotations);

        return region;
    }

    private Double extractDouble(Configuration config, String key, boolean isRequired, Predicate<Double> valueChecker) {
        List<Double> values = extractDoubleList(config, key, isRequired, 1, valueChecker);
        return values.isEmpty() ? null : values.get(0);
    }

    private Integer extractInteger(Configuration config, String key, boolean isRequired, Predicate<Integer> valueChecker) {
        List<Integer> values = extractIntegerList(config, key, isRequired, 1, valueChecker);
        return values.isEmpty() ? null : values.get(0);
    }

    private String extractString(Configuration config, String key, boolean isRequired, Predicate<String> valueChecker) {
        List<String> values = extractStringList(config, key, isRequired, 1, valueChecker);
        return values.isEmpty() ? null : values.get(0);
    }

    private List<Double> extractDoubleList(
            Configuration config, String key, boolean isRequired, int requiredSize, 
            Predicate<Double> valueChecker) {
        return extractList(config, key, isRequired,
                (n) -> n == requiredSize,
                (s) -> DoubleValidator.getInstance().validate(s, Locale.US),
                valueChecker);
    }

    private List<Integer> extractIntegerList(
            Configuration config, String key, boolean isRequired, 
            int requiredSize, Predicate<Integer> valueChecker) {
        return extractList(config, key, isRequired, 
                (n) -> n == requiredSize, 
                (s) -> IntegerValidator.getInstance().validate(s, Locale.US), 
                valueChecker);
    }

    
    private List<String> extractStringList(
            Configuration config, String key, boolean isRequired, 
            int requiredLength, Predicate<String> valueChecker) {
        return extractList(config, key, isRequired, 
                (n) -> n == requiredLength, (s) -> s, valueChecker);
    }

    private Ratio extractRatio(Configuration config, String key, boolean isRequired, int requiredSize) {
        List<Double> ratioData = extractList(config, key, isRequired, 
                () -> config.getString(key).split("[ :]"),
                (n) -> n == requiredSize, 
                (s) -> DoubleValidator.getInstance().validate(s, Locale.US), 
                (d) -> d >= 0);
        if (ratioData.isEmpty())
            return null;
        Ratio ratio = new Ratio(ratioData);
        Validate.isTrue(ratio.getTotal() > 0, "%s: Invalid ratio", key);
        return ratio;
    }
    
    private <T> List<T> extractList(Configuration config, String key, boolean isRequired, 
            Predicate<Integer> sizeChecker,            
            Function<String, T> valueConverter, 
            Predicate<T> valueChecker) 
    {
        return extractList(config, key, isRequired, () -> config.getStringArray(key), sizeChecker, valueConverter, valueChecker);
    }
    
     private <T> List<T> extractList(Configuration config, String key, boolean isRequired, 
            Supplier<String[]> valuesSupplier,
            Predicate<Integer> sizeChecker,            
            Function<String, T> valueConverter, 
            Predicate<T> valueChecker) 
    {
        List<T> values = new ArrayList<>();
        if (config.containsKey(key)) {
            String[] valuesStrings = valuesSupplier.get();
            Validate.isTrue(sizeChecker.test(valuesStrings.length), "%s: Invalid number of entries (%d)", key, valuesStrings.length);
            for (String s : valuesStrings) {
                T value = valueConverter.apply(s);
                Validate.notNull(value, "%s: Invalid entry (%s)", key, s);
                Validate.isTrue(valueChecker.test(value), "%s: Invalid value (%s)", key, value);
                values.add(value);
            }
        } else
            Validate.validState(!isRequired, "%s: Missing key", key);
        return values;
       
    }   
    
    private <T> List<T> extractList(Configuration config,
            String key, boolean isRequired, Predicate<Integer> sizeChecker, 
            BiFunction<String, Integer, T> valueConverter,
            BiPredicate<T, Integer> valueChecker) {
        List<T> values = new ArrayList<>();
        if (config.containsKey(key)) {
            String[] valuesStrings = config.getStringArray(key);
            Validate.isTrue(sizeChecker.test(valuesStrings.length), "%s: Invalid number of entries (%d)", key, valuesStrings.length);
            for (int i = 0; i < valuesStrings.length; i++) {
                T value = valueConverter.apply(valuesStrings[i], i);
                Validate.notNull(value, "%s: Invalid entry (%s)", key, valuesStrings[i]);
                Validate.isTrue(valueChecker.test(value, i), "%s: Invalid value (%s)", key, value);
                values.add(value);
            }
        } else
            Validate.validState(!isRequired, "%s: Missing key", key);
        return values;
    }

    private void distributeExamples() {
        _learnTestDistribution = calculateDistribution(_learnTestRatio, _numExamples);

        log.debug("Distributing all examples into learning and testing parts: #{} ({}) ==> #{}", getNumExamples(), _learnTestRatio, _learnTestDistribution);
        for (int s: Arrays.asList(Ratio.LEARN, Ratio.TEST)) {
            if (_learnTestDistribution.get(s) == 0) 
                continue;
            log.debug(s == Ratio.LEARN ? "Learning part" : "Testing part");
            Ratio classDistribution = calculateDistribution(_classRatio, (int) _learnTestDistribution.get(s));
            setClassDitribution(s, classDistribution);
            log.debug("  Distributing examples into classes: #{} ({}) ==> #{} (initial attempt)", _learnTestDistribution.get(s), _classRatio, classDistribution);
            boolean corrected;
            do {
                corrected = false;
                for (int c = 0; c < getNumClasses(); c++) {
                    DecisionClass clazz = getDecisionClass(c);
                    int numExamples = (int) getClassDistribution(s).get(c);
                    Ratio exampleTypeDistribution = calculateDistribution(clazz.getExampleTypeRatio(), numExamples, true);
                    log.debug("    Distributing class {} into examples types: #{} ({}) ==> #{}", c + 1, numExamples, clazz.getExampleTypeRatio(), exampleTypeDistribution);       
                    // Correction of RARE cases is necessary only for learning set (we need to have
                    // sufficiently "large" groups of rare cases. For testing set we select 
                    // "representives" of these grops
                    if (s == Ratio.LEARN) {
                        int numRare = (int) exampleTypeDistribution.get(Ratio.RARE);
                        int numRemaining = numRare % OutlierType.RARE_CASE.numLearnExamplesPerGroup();
                        if (numRare == exampleTypeDistribution.getTotal() && numRemaining > 0) {
                            log.debug("   Class {} needs to be corrected/adjusted [{} remaining/missing RARE examples]", c + 1, numRemaining);
                            corrected = true;
                            int d = (int) classDistribution.get(c);
                            if (numRemaining >= OutlierType.RARE_CASE.numLearnExamplesPerGroup() / 2.0) 
                                classDistribution.set(c, d + OutlierType.RARE_CASE.numLearnExamplesPerGroup() - numRemaining);
                            else
                                classDistribution.set(c, d - numRemaining);     
                            setClassDitribution(s, classDistribution);
                        }
                    }
                    clazz.setExampleTypeDistribution(s, exampleTypeDistribution);
                }
            } while (corrected);
            log.debug("  Distributing examples into classes: #{} ({}) ==> #{} (after possible correction)", _learnTestDistribution.get(s), _classRatio, classDistribution);
            setClassDitribution(s, classDistribution);
            _learnTestDistribution.set(s, classDistribution.getTotal());
            
            for (int c = 0; c < getNumClasses(); c++) {
                DecisionClass clazz = getDecisionClass(c);
                log.debug("    Distributing class {} into examples types: #{} ({}) ==> #{}", c + 1, clazz.getNumExamples(s),clazz.getExampleTypeRatio(), clazz.getExampleTypeDistribution(s));                    
                distributeExamplesIntoRegions(s, c);
            }
        }
        
    }
    
    private void distributeExamplesIntoRegions(int setIndex, int classIndex) {
        DecisionClass clazz = getDecisionClass(classIndex);
        
        int[] numRemaining = new int[] {(int) clazz.getExampleTypeDistribution(setIndex).get(Ratio.SAFE), (int) clazz.getExampleTypeDistribution(setIndex).get(Ratio.BORDER)};        
        int numSafeBorder = numRemaining[Ratio.SAFE] + numRemaining[Ratio.BORDER];
        Ratio regionDistribution = calculateDistribution(clazz.getRegionRatio(), numSafeBorder);                
        clazz.setRegionDistribution(setIndex, regionDistribution);
        Ratio safeBorderFractions = clazz.getExampleTypeRatio().subRatio(Ratio.SAFE, 2).toFractions();
        log.debug("      Ratio of SAFE:BORDER examples: {}", safeBorderFractions);
        int[][] numDistributed = new int[clazz.getNumRegions()][2];
        for (int r = 0; r < clazz.getNumRegions(); r++) {
            for (int t : Arrays.asList(Ratio.SAFE, Ratio.BORDER)) {
                numDistributed[r][t] = Math.min((int) Math.floor(regionDistribution.get(r)*safeBorderFractions.get(t)), numRemaining[t]);
                numRemaining[t] -= numDistributed[r][t];                
            }
        }
        
        while (numRemaining[Ratio.SAFE] + numRemaining[Ratio.BORDER] > 0) {
            for (int t : Arrays.asList(Ratio.SAFE, Ratio.BORDER)) {
                if (numRemaining[t] > 0) {
                    int foundRegionIndex = -1;
                    double minFraction = 1.0;
                    for (int r = 0; r < clazz.getNumRegions(); r++) {
                        if (numDistributed[r][Ratio.SAFE] + numDistributed[r][Ratio.BORDER] < regionDistribution.get(r)) {
                            double fraction = 0.0;
                            if ((numDistributed[r][Ratio.SAFE] + numDistributed[r][Ratio.BORDER]) > 0) 
                                fraction = (double) numDistributed[r][t] / (numDistributed[r][Ratio.SAFE] + numDistributed[r][Ratio.BORDER]);
                            if (fraction <= minFraction) {
                                foundRegionIndex = r;
                                minFraction = fraction;
                            }
                        }
                    }
                    if (foundRegionIndex != -1) {
                        numDistributed[foundRegionIndex][t]++;
                        numRemaining[t]--;
                    }
                }
            }
        }
        
        for (int r = 0 ; r < clazz.getNumRegions(); r++) {
            Region region = clazz.getRegion(r);
            region.setExampleTypeDistribution(setIndex, new Ratio((double) numDistributed[r][Ratio.SAFE], (double) numDistributed[r][Ratio.BORDER]));
            log.debug("      Distributing region {} into examples types: #{} ==> {}", r + 1, region.getNumExamples(setIndex), region.getExampleTypeDistribution(setIndex));
        }
          
    }
    
    
    private Ratio calculateDistribution(Ratio ratio, int total) {
        return calculateDistribution(ratio, total, false);
    }
    
    private Ratio calculateDistribution(Ratio ratio, int total, boolean correctedForRare){
        Ratio fractions = ratio.toFractions();
        Ratio distribution = new Ratio(ratio.size(), 0.0);
        
        if (fractions.getTotal() != 0) {
            double subTotal = 0.0;

            if (correctedForRare) {
                double rareFraction = fractions.get(Ratio.RARE);
                if (rareFraction > 0) {
                    double rawRare = rareFraction * total;
                    double numRare = Math.floor(rawRare / NUM_RARE_PER_GROUP) * NUM_RARE_PER_GROUP;
                    double rawRemaining = rawRare - numRare;
                    if (rawRemaining >= 0.5*NUM_RARE_PER_GROUP) 
                        numRare += NUM_RARE_PER_GROUP;
                    if (numRare > total)
                        numRare = total;
                    distribution.set(Ratio.RARE, numRare);
                    subTotal += numRare;
                }

                // RARE cases have been processed, so they can be discarded now
                fractions.set(Ratio.RARE, 0.0);
            }
            int maxFractionIndex = fractions.sortIndexes(false).get(0);
            for (int i = 0; i < fractions.size(); i++) 
                if (i != maxFractionIndex && (correctedForRare ? i != Ratio.RARE : true)) {
                    distribution.set(i, Math.round(total * fractions.get(i)));
                    subTotal += distribution.get(i);
                }
            distribution.set(maxFractionIndex, total - subTotal);
            Validate.validState(distribution.get(maxFractionIndex) >= 0);
        }
        return distribution;

    }
    
    public DecisionClass getDecisionClass(int classIndex) {
        return _decisionClasses.get(classIndex);
    }
    
    public Ratio getClassDistribution(int setIndex) {
        return _classDistributions[setIndex];
    }
    
    public GeneratorSettings setClassDitribution(int setIndex, Ratio distribution) {
        _classDistributions[setIndex] = new Ratio(distribution);
        return this;
    }
    
    public String getAttributeName(int attrIndex) {
        return _attributeNames.get(attrIndex);
    }
    
    public String getClassName(int classIndex) {
        return _classNames.get(classIndex);
    }

    /**
     * Checks settings for validity
     * @throws ConfigurationException 
     */
    private void validate() throws ConfigurationException {
        for (int c = 0; c < getNumClasses(); c++) {
            DecisionClass clazz = getDecisionClass(c);
            int numIntegumentalRegions = 0;
            Ratio exampleTypeRatio = clazz.getExampleTypeRatio();
            for (int r = 0; r < clazz.getNumRegions(); r++) {
                Region region = clazz.getRegion(r);
                if (region.getShape() == ShapeType.INTEGUMENTAL) {
                    numIntegumentalRegions++;
                    if (exampleTypeRatio.getTotal() != exampleTypeRatio.get(Ratio.SAFE))
                        throw new ConfigurationException(String.format("Class %d: borderline, rare and outlier examples not allowed for integumetnal regions", c + 1));
                    if (region.getNumRotations() > 0)
                        throw new ConfigurationException(String.format("Class %d, region %d: no rotations are allowed for integumetnal regions", c + 1, r + 1));
                } else {
                    if (region.getBorder() == BorderType.FIXED && exampleTypeRatio.get(Ratio.BORDER) > 0 && region.getBorderZone() == 0.0)
                        throw new ConfigurationException(String.format("Class %d, region %d: border zone has no area, generating borederline example is impossible", c + 1, r + 1));
                        
                }
            }
            if (numIntegumentalRegions > 0 && numIntegumentalRegions != clazz.getNumRegions())
                throw new ConfigurationException(String.format("Class %d: integumental regions cannot be combined with other regions", c + 1));
        }
    }
    
    public String getFileName(int setIndex) {
        return setIndex == Ratio.LEARN ? getLearnFileName() : getTestFileName();
    }
    
    public String getFileName() {
        return getFileName(Ratio.LEARN);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("Final configuration:\nattributes = %d\nclasses = %d\nexamples = %d\n", getNumAttributes(),getNumClasses(), getNumExamples());
        formatter.format("names.attributes = %s\nnames.classes = %s\nnames.decision = %s\n", 
                String.join(", ", getAttributeNames()), String.join(", ", getClassNames()), getDecisionName());
        if (getNumLearnTestPairs() > 0) {
            formatter.format("learnTestRatio = %s\nlearnTestPairs = %d\nfileName.learn = %s\nfileName.test = %s\n", 
                    getLearnTestRatio(), getNumLearnTestPairs(), getLearnFileName(), getTestFileName());
        } else
            formatter.format("fileName = %s\n", getLearnFileName());
        formatter.format("minOutlierDistance = %s\n", getMinOutlierDistance());
        formatter.format("classRatio = %s\n", getClassRatio());
        for (int c = 0; c < getNumClasses(); c++) {
            DecisionClass clazz = getDecisionClass(c);
            String classString = String.format("class.%d", c + 1);
            formatter.format("%s.exampleTypeRatio = %s\n", classString, clazz.getExampleTypeRatio());
            formatter.format("%s.regions = %d\n", classString, clazz.getNumRegions());
            for (int r = 0; r < clazz.getNumRegions(); r++) {
                Region region = clazz.getRegion(r);
                String regionString = String.format("class.%d.region.%d", c + 1, r + 1);
                formatter.format("%s.weight = %s\n", regionString, region.getWeight());
                formatter.format("%s.shape = %s\n", regionString, region.getShape());
                formatter.format("%s.distribution = %s\n", regionString, region.getDistribution());
                formatter.format("%s.center = %s\n", regionString, region.getCenter());
                formatter.format("%s.radius = %s\n", regionString, region.getRadius());
                formatter.format("%s.borderZone = %s\n", regionString, region.getBorderZone());
                formatter.format("%s.noOutlierZone = %s\n", regionString, region.getNoOutlierZone());
                formatter.format("%s.rotations = %s\n", regionString, region.getRotations());
            }
        }
        if (!getLabeledClassIndexes().isEmpty()) {            
            formatter.format("exampleTypeLabels.classes = %s\n", getLabeledClassIndexes().stream().map((n) -> n + 1).map(Object::toString).collect(Collectors.joining(", ")));
        }
        
        return sb.toString();
    }
}

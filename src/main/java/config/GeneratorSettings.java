package config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.DoubleValidator;
import org.apache.commons.validator.routines.IntegerValidator;

/**
 * Keeps all configuration settings extracted from a configuration file and from
 * a command line
 *
 * @author swilk
 */
@ToString
@Getter
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
    private final static String KEY_OUTPUT_MODE = "outputMode";
    private final static String KEY_FILE_NAME = "fileName";
    private final static String KEY_LEARN = "learn";
    private final static String KEY_TEST = "test";
    private final static String KEY_LEARN_TEST_RATIO = "learnTestRatio";
    private final static String KEY_LEARN_TEST_PAIRS = "learnTestPairs";

    private static final String NAME_DECISION = "D";
    private static final String NAME_CLASSS = "%d";
    private static final String NAME_ATTRIBUTTE = "X%d";



    private final static int SIZE_EXAMPLE_TYPES_RATIO = 4;
    private final static int SIZE_LEARN_TEST_RATIO = 2;
    
    private List<Class> _classes = null;
    private Ratio _classRatio = null;

    private double _minOutlierDistance = 0;
    
    private int _numExamples = 0;
    private int _numAttributes = 0;
    
    private OutputMode _outputMode = OutputMode.SINGLE;
    private int _numLearnTestPairs = 1;
    private Ratio _learnTestRatio = null;
    
    private String _learnFileName;
    private String _testFileName;
    
    private List<String> _attributeNames = null;
    private List<String> _classNames = null;
    private String _decisionName;
    
    public int getNumClasses() {
        return _classes.size();
    }

    public void read(String fileName, Properties cmdLineProperties) throws ConfigurationException {
        CompositeConfiguration config = new CompositeConfiguration();

        config.addConfiguration(new MapConfiguration(cmdLineProperties));
        config.addConfiguration(new PropertiesConfiguration(fileName));
        
        // Read information about attributes (currently limited to their number)
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
            _classes.get(c).setRegions(regions).setRegionRatio(new Ratio(regionRatioData));
        }
        
        // Examples
        _numExamples = extractInteger(config, KEY_EXAMPLES, true, (n) -> n > 0);
        
        // Output data/files
        String outputString = extractString(config, KEY_OUTPUT_MODE, false, (s) -> OutputMode.isValid(s));
        if (outputString != null)
            _outputMode = OutputMode.validate(outputString);
        else
            _outputMode = OutputMode.SINGLE;
                
        if (_outputMode == OutputMode.SINGLE) {
            _numLearnTestPairs = 0;
            _learnFileName = extractString(config, KEY_FILE_NAME, true, (s) -> !s.isEmpty());
            _testFileName = StringUtils.EMPTY;
            _learnTestRatio = null;
        } else {
            _numLearnTestPairs = extractInteger(config, KEY_LEARN_TEST_PAIRS, true, (n) -> n >= 1);
            key = String.format("%s.%s", KEY_FILE_NAME, KEY_LEARN);
            _learnFileName = extractString(config, key, true, (s) -> !s.isEmpty());
            key = String.format("%s.%s", KEY_FILE_NAME, KEY_TEST);
            _testFileName = extractString(config, key, true, (s) -> !s.isEmpty());
            _learnTestRatio = extractRatio(config, KEY_LEARN_TEST_RATIO, true, SIZE_LEARN_TEST_RATIO);                       
        }
        System.out.println(toString());
    }

    private void readClasses(Configuration config) throws ConfigurationException {
        String key = KEY_CLASSES;
        int numClasses = extractInteger(config, key, true, (n) -> n > 0);
        _classRatio = extractRatio(config, KEY_CLASS_RATIO, true, numClasses);
        
        key = String.format("%s.%s", KEY_DEFAULT_CLASS, KEY_EXAMPLE_TYPE_RATIO);
        Ratio defaultExampleTypeRatio = extractRatio(config, key, false, SIZE_EXAMPLE_TYPES_RATIO);
        if (defaultExampleTypeRatio == null)
            defaultExampleTypeRatio = new Ratio(Arrays.asList(100.0, 0.0, 0.0, 0.0));
        
        _classes = new ArrayList<>();
        for (int c = 0; c < numClasses; c++) {
            Class clazz = new Class(); 
            key = String.format("%s.%d.%s", KEY_CLASS, c + 1, KEY_EXAMPLE_TYPE_RATIO);            
            Ratio exampleTypeRatio = extractRatio(config, key, false, SIZE_EXAMPLE_TYPES_RATIO);
            if (exampleTypeRatio == null)
                exampleTypeRatio = defaultExampleTypeRatio;
            clazz.setExampleTypeRatio(exampleTypeRatio);
            _classes.add(clazz);            
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
        
        key = String.format("%s.%s", baseKey, KEY_BORDER_ZONE);
        Double borderZone = extractDouble(config, key, 
                !isDefault && region.getBorderZone() == null, (d) -> d >= 0);
        if (borderZone != null) 
            region.setBorderZone(borderZone);
                        
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
                distribution.setNumStandardDeviations((Double) distData.get(1));
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
                
        if (rotationsData.size() > 0) {
            List<Rotation> rotations = new ArrayList<>();
            for (int i = 0; i < rotationsData.size(); i += 3) {
                int axis1 = (Integer) rotationsData.get(i);
                int axis2 = (Integer) rotationsData.get(i + 1);
                double angle = (Double) rotationsData.get(i + 2);
                rotations.add(new Rotation(axis1, axis2, angle));
            }
            region.setRotations(rotations);
        }

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

    public static void main(String[] args) throws ConfigurationException {
        if (args.length == 0) {
            return;
        }
        String fileName = args[0];

        GeneratorSettings dataConfig = new GeneratorSettings();
        dataConfig.read(fileName, new Properties());
    }

}

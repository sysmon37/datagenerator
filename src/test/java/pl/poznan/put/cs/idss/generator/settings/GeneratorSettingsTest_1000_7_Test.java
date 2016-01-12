package pl.poznan.put.cs.idss.generator.settings;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.commons.configuration.ConfigurationException;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author swilk
 */
public class GeneratorSettingsTest_1000_7_Test {
    private GeneratorSettings _settings = null;
    private static final String CONFIG = "paw3-2d.conf";
    private final Properties _properties = new Properties();
        
    public GeneratorSettingsTest_1000_7_Test() {
        _properties.setProperty("classRatio", "1:7");
        _properties.setProperty("examples", "1000");
        _properties.setProperty("learnTestRatio", "1:0");
    }
    
    @Before
    public void setUp() {
        _settings = new GeneratorSettings();
    }
    
    @Test
    public void distributeExamples_checkTotals() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "25:25:25:25");
        _settings.read(CONFIG, _properties);

        // Check classes and regions
        for (int c = 0; c < _settings.getNumClasses(); c++) {
            double total = 0;
            for (int r = 0; r < _settings.getClass(c).getNumRegions(); r++)
                total += _settings.getClass(c).getRegion(r).getNumExamples(Ratio.LEARN);
            total += _settings.getClass(c).getNumExamples(Ratio.LEARN, Ratio.RARE);
            total += _settings.getClass(c).getNumExamples(Ratio.LEARN, Ratio.OUTLIER);            
            assertEquals(_settings.getClass(c).getNumExamples(Ratio.LEARN), total, 0.0);
        }
    }

    @Test
    public void distributeExamples_90_10_0_0() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "90:10:0:0");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(112.0, 13.0, 0.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(37.0, 4.0), new Ratio(38.0, 4.0), new Ratio(37.0, 5.0)));
    }

    @Test
    public void distributeExamples_70_30_0_0() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "70:30:0:0");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);

        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(87.0, 38.0, 0.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(29.0, 12.0), new Ratio(29.0, 13.0), new Ratio(29.0, 13.0)));        
    }

    @Test
    public void distributeExamples_60_30_0_10() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "60:30:0:10");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(74.0, 38.0, 0.0, 13.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(25.0, 13.0), new Ratio(25.0, 12.0), new Ratio(24.0, 13.0)));        
    }
    
    @Test
    public void distributeExamples_60_20_10_10() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "60:20:10:10");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(75.0, 25.0, 12.0, 13.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(25.0, 9.0), new Ratio(25.0, 8.0), new Ratio(25.0, 8.0)));        
    }
    
    @Test
    public void distributeExamples_50_30_10_10() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "50:30:10:10");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);

        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(62.0, 38.0, 12.0, 13.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(21.0, 13.0), new Ratio(21.0, 12.0), new Ratio(20.0, 13.0)));            
    }

    @Test
    public void distributeExamples_40_40_10_10() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "40:40:10:10");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(50.0, 50.0, 12.0, 13.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(17.0, 17.0), new Ratio(16.0, 17.0), new Ratio(17.0, 16.0)));        
    }

    @Test
    public void distributeExamples_30_50_10_10() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "30:50:10:10");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(38.0, 62.0, 12.0, 13.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(13.0, 21.0), new Ratio(12.0, 21.0), new Ratio(13.0, 20.0)));        
    }

    @Test
    public void distributeExamples_20_60_10_10() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "20:60:10:10");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(25.0, 75.0, 12.0, 13.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(9.0, 25.0), new Ratio(8.0, 25.0), new Ratio(8.0, 25.0)));        
    }

    @Test
    public void distributeExamples_10_60_20_10() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "10:60:20:10");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(13.0, 73.0, 26.0, 13.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(4.0, 25.0), new Ratio(4.0, 24.0), new Ratio(5.0, 24.0)));        
    }

    @Test
    public void distributeExamples_10_50_20_20() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "10:50:20:20");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(13.0, 61.0, 26.0, 25.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(4.0, 21.0), new Ratio(4.0, 20.0), new Ratio(5.0, 20.0)));        
    }

    @Test
    public void distributeExamples_10_40_20_30() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "10:40:20:30");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(13.0, 48.0, 26.0, 38.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(4.0, 16.0), new Ratio(4.0, 16.0), new Ratio(5.0, 16.0)));        
    }

    @Test
    public void distributeExamples_10_30_30_30() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "10:30:30:30");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(13.0, 36.0, 38.0, 38.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(4.0, 12.0), new Ratio(4.0, 12.0), new Ratio(5.0, 12.0)));        
    }

    @Test
    public void distributeExamples_0_40_30_30() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:40:30:30");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 49.0, 38.0, 38.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(0.0, 16.0), new Ratio(0.0, 16.0), new Ratio(0.0, 17.0)));        
    }

    @Test
    public void distributeExamples_0_30_30_40() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:30:30:40");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 38.0, 38.0, 49.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(0.0, 12.0), new Ratio(0.0, 13.0), new Ratio(0.0, 13.0)));        
    }

    @Test
    public void distributeExamples_0_20_30_50() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:20:30:50");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 25.0, 38.0, 62.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(0.0, 8.0), new Ratio(0.0, 8.0), new Ratio(0.0, 9.0)));        
    }

    @Test
    public void distributeExamples_0_10_30_60() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:10:30:60");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 13.0, 38.0, 74.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(0.0, 4.0), new Ratio(0.0, 4.0), new Ratio(0.0, 5.0)));        
    }

    @Test
    public void distributeExamples_0_0_30_70() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:0:30:70");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 38.0, 87.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(0.0, 0.0), new Ratio(0.0, 0.0), new Ratio(0.0, 0.0)));        
    }

    @Test
    public void distributeExamples_0_0_10_90() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:0:10:90");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(125.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 12.0, 113.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
        // Check example types in regions
        List<Ratio> actualDistributions = Arrays.asList(
                minorityClass.getRegion(0).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(1).getExampleTypeDistribution(Ratio.LEARN),
                minorityClass.getRegion(2).getExampleTypeDistribution(Ratio.LEARN));
        assertThat(actualDistributions, containsInAnyOrder(new Ratio(0.0, 0.0), new Ratio(0.0, 0.0), new Ratio(0.0, 0.0)));        
    }

 
}

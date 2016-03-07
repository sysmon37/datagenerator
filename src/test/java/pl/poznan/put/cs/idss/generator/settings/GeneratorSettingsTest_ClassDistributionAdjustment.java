package pl.poznan.put.cs.idss.generator.settings;

import pl.poznan.put.cs.idss.generator.settings.GeneratorSettings;
import pl.poznan.put.cs.idss.generator.settings.Class;
import pl.poznan.put.cs.idss.generator.settings.Ratio;
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
public class GeneratorSettingsTest_ClassDistributionAdjustment {
    private GeneratorSettings _settings = null;
    private static final String CONFIG = "paw3-2d.conf";
    private final Properties _properties = new Properties();
        
    public GeneratorSettingsTest_ClassDistributionAdjustment() {
        _properties.setProperty("classRatio", "1:7");
        _properties.setProperty("examples", "1000");
    }
    
    @Before
    public void setUp() {
        _settings = new GeneratorSettings();
    }
    
    @Test
    public void distributeExamples_onlyRareInMinorityClass_trainNoTest() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _properties.setProperty("learnTestRatio", "100:0");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(126.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 126.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
    }

    @Test
    public void distributeExamples_onlyRareInBothClasses_trainNoTest() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _properties.setProperty("class.2.exampleTypeRatio", "0:0:100:0");
        _properties.setProperty("learnTestRatio", "100:0");
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(126.0, 876.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 126.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 876.0, 0.0)));
    }
    
    
    @Test
    public void distributeExamples_onlyRareInMinorityClass_trainAndTest() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _properties.setProperty("learnTestRatio", "875:125");
        _properties.setProperty("outputMode", "split");
        _properties.setProperty("learnTestPairs", "1");
        _properties.setProperty("fileName.learn", "learn%d");
        _properties.setProperty("fileName.test", "test%d");
        
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        
        // Train-test ratio
        assertThat(_settings.getLearnTestRatio(), is(new Ratio(875.0, 125.0)));
        // Train set
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(110.0, 766.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 110.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(766.0, 0.0, 0.0, 0.0)));

        // Train set
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.TEST), is(new Ratio(16.0, 109.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.TEST), is(new Ratio(0.0, 0.0, 16.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.TEST), is(new Ratio(109.0, 0.0, 0.0, 0.0)));

    }

    @Test
    public void distributeExamples_onlyRareInBothClasses_trainAndTest_875_125() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _properties.setProperty("class.2.exampleTypeRatio", "0:0:100:0");
        _properties.setProperty("learnTestRatio", "875:125");
        _properties.setProperty("outputMode", "split");
        _properties.setProperty("learnTestPairs", "1");
        _properties.setProperty("fileName.learn", "learn%d");
        _properties.setProperty("fileName.test", "test%d");
        
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        
        // Train-test ratio
        assertThat(_settings.getLearnTestRatio(), is(new Ratio(875.0, 125.0)));
        // Train set
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(110.0, 766.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 110.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 766.0, 0.0)));

        // Train set
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.TEST), is(new Ratio(16.0, 109.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.TEST), is(new Ratio(0.0, 0.0, 16.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.TEST), is(new Ratio(0.0, 0.0, 109.0, 0.0)));

    }

    @Test
    public void distributeExamples_onlyRareInBothClasses_trainAndTest_710_290() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _properties.setProperty("class.2.exampleTypeRatio", "0:0:100:0");
        _properties.setProperty("learnTestRatio", "710:290");
        _properties.setProperty("outputMode", "split");
        _properties.setProperty("learnTestPairs", "1");
        _properties.setProperty("fileName.learn", "learn%d");
        _properties.setProperty("fileName.test", "test%d");
        
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        
        // Train-test ratio
        assertThat(_settings.getLearnTestRatio(), is(new Ratio(710.0, 290.0)));
        // Train set
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(90.0, 622.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 90.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 622.0, 0.0)));

        // Train set
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.TEST), is(new Ratio(36.0, 254.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.TEST), is(new Ratio(0.0, 0.0, 36.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.TEST), is(new Ratio(0.0, 0.0, 254.0, 0.0)));
    }

    @Test
    public void distributeExamples_onlyRareInBothClasses_trainAndTest_500_500() throws ConfigurationException {
        _properties.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _properties.setProperty("class.2.exampleTypeRatio", "0:0:100:0");
        _properties.setProperty("learnTestRatio", "500:500");
        _properties.setProperty("outputMode", "split");
        _properties.setProperty("learnTestPairs", "1");
        _properties.setProperty("fileName.learn", "learn%d");
        _properties.setProperty("fileName.test", "test%d");
        
        _settings.read(CONFIG, _properties);
      
        Class minorityClass = _settings.getClass(0);
        Class majorityClass = _settings.getClass(1);
        
        // Train-test ratio
        assertThat(_settings.getLearnTestRatio(), is(new Ratio(500.0, 500.0)));
        // Train set
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(64.0, 438.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 64.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 438.0, 0.0)));

        // Train set
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.TEST), is(new Ratio(63.0, 437.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.TEST), is(new Ratio(0.0, 0.0, 63.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.TEST), is(new Ratio(0.0, 0.0, 437.0, 0.0)));

    }

}

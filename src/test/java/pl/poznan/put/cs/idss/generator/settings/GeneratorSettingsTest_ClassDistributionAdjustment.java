package pl.poznan.put.cs.idss.generator.settings;

import pl.poznan.put.cs.idss.generator.settings.GeneratorSettings;
import pl.poznan.put.cs.idss.generator.settings.DecisionClass;
import pl.poznan.put.cs.idss.generator.settings.Ratio;
import java.util.Properties;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.MapConfiguration;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author swilk
 */
public class GeneratorSettingsTest_ClassDistributionAdjustment {
    private GeneratorSettings _settings = new GeneratorSettings();
    private static final String CONFIG = "paw3-2d.conf";
    private final Properties _common = new Properties();
    private final Properties _changed = new Properties();
        
    public GeneratorSettingsTest_ClassDistributionAdjustment() {
        _common.setProperty("attributes", "2");
        _common.setProperty("classes", "2");
        _common.setProperty("classRatio", "1:7");
        _common.setProperty("examples", "1000");
        _common.setProperty("learnTestRatio", "1:0");
        _common.setProperty("minOutlierDistance", "1");
        _common.setProperty("defaultRegion.weight", "1");
        _common.setProperty("defaultRegion.distribution", "U");
        _common.setProperty("defaultRegion.borderZone", "3");
        _common.setProperty("defaultRegion.noOutlierZone", "1.5");
        _common.setProperty("defaultRegion.shape", "C");
        _common.setProperty("defaultRegion.radius", "2, 1");
        _common.setProperty("defaultClass.exampleTypeRatio", "100:0:0:0");
        _common.setProperty("class.1.regions", "3");
        _common.setProperty("class.1.region.1.center", "5, 5");
        _common.setProperty("class.1.region.1.rotation", "1, 2, 45");
        _common.setProperty("class.1.region.2.center", "-5, 3");
        _common.setProperty("class.1.region.2.rotation", "1, 2, -45");
        _common.setProperty("class.1.region.3.center", "0, -5");
        _common.setProperty("class.2.regions", "1");
        _common.setProperty("class.2.region.1.shape", "I");
        _common.setProperty("class.2.region.1.center", "0, 0");
        _common.setProperty("class.2.region.1.radius", "10, 10");
        _common.setProperty("fileName", "paw3-2d.arff");        
    }
    
    @Before
    public void setUp() {
       _settings = new GeneratorSettings();
   }
    
    @Test
    public void distributeExamples_onlyRareInMinorityClass_trainNoTest() throws ConfigurationException {
        _changed.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _changed.setProperty("learnTestRatio", "100:0");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(_changed));
        config.addConfiguration(new MapConfiguration(_common));        
        _settings.read(config);
      
        DecisionClass minorityClass = _settings.getDecisionClass(0);
        DecisionClass majorityClass = _settings.getDecisionClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(126.0, 875.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 126.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(875.0, 0.0, 0.0, 0.0)));
    }

    @Test
    public void distributeExamples_onlyRareInBothClasses_trainNoTest() throws ConfigurationException {
        _changed.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _changed.setProperty("class.2.exampleTypeRatio", "0:0:100:0");
        _changed.setProperty("learnTestRatio", "100:0");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(_changed));
        config.addConfiguration(new MapConfiguration(_common));        
        _settings.read(config);
      
        DecisionClass minorityClass = _settings.getDecisionClass(0);
        DecisionClass majorityClass = _settings.getDecisionClass(1);
        // Check classes
        assertThat(_settings.getClassDistribution(Ratio.LEARN), is(new Ratio(126.0, 876.0)));
        // Check example types in classes
        assertThat(minorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 126.0, 0.0)));
        assertThat(majorityClass.getExampleTypeDistribution(Ratio.LEARN), is(new Ratio(0.0, 0.0, 876.0, 0.0)));
    }
    
    
    @Test
    public void distributeExamples_onlyRareInMinorityClass_trainAndTest() throws ConfigurationException {
        _changed.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _changed.setProperty("learnTestRatio", "875:125");
        _changed.setProperty("outputMode", "split");
        _changed.setProperty("learnTestPairs", "1");
        _changed.setProperty("fileName.learn", "learn%d");
        _changed.setProperty("fileName.test", "test%d");
        
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(_changed));
        config.addConfiguration(new MapConfiguration(_common));        
        _settings.read(config);
      
        DecisionClass minorityClass = _settings.getDecisionClass(0);
        DecisionClass majorityClass = _settings.getDecisionClass(1);
        
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
        _changed.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _changed.setProperty("class.2.exampleTypeRatio", "0:0:100:0");
        _changed.setProperty("learnTestRatio", "875:125");
        _changed.setProperty("outputMode", "split");
        _changed.setProperty("learnTestPairs", "1");
        _changed.setProperty("fileName.learn", "learn%d");
        _changed.setProperty("fileName.test", "test%d");
        
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(_changed));
        config.addConfiguration(new MapConfiguration(_common));        
        _settings.read(config);
      
        DecisionClass minorityClass = _settings.getDecisionClass(0);
        DecisionClass majorityClass = _settings.getDecisionClass(1);
        
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
        _changed.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _changed.setProperty("class.2.exampleTypeRatio", "0:0:100:0");
        _changed.setProperty("learnTestRatio", "710:290");
        _changed.setProperty("outputMode", "split");
        _changed.setProperty("learnTestPairs", "1");
        _changed.setProperty("fileName.learn", "learn%d");
        _changed.setProperty("fileName.test", "test%d");
        
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(_changed));
        config.addConfiguration(new MapConfiguration(_common));        
        _settings.read(config);
      
        DecisionClass minorityClass = _settings.getDecisionClass(0);
        DecisionClass majorityClass = _settings.getDecisionClass(1);
        
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
        _changed.setProperty("class.1.exampleTypeRatio", "0:0:100:0");
        _changed.setProperty("class.2.exampleTypeRatio", "0:0:100:0");
        _changed.setProperty("learnTestRatio", "500:500");
        _changed.setProperty("outputMode", "split");
        _changed.setProperty("learnTestPairs", "1");
        _changed.setProperty("fileName.learn", "learn%d");
        _changed.setProperty("fileName.test", "test%d");
        
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(_changed));
        config.addConfiguration(new MapConfiguration(_common));        
        _settings.read(config);
      
        DecisionClass minorityClass = _settings.getDecisionClass(0);
        DecisionClass majorityClass = _settings.getDecisionClass(1);
        
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

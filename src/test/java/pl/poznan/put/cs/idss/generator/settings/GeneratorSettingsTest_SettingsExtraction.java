package pl.poznan.put.cs.idss.generator.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author swilk
 */
public class GeneratorSettingsTest_SettingsExtraction {

    private GeneratorSettings _settings = null;
    private final Properties _common = new Properties();
    
    public GeneratorSettingsTest_SettingsExtraction() {
        _common.setProperty("classRatio", "1:7");
        _common.setProperty("examples", "1000");
        
        // set common properties
        _common.setProperty("attributes", "2");
        _common.setProperty("classes", "2");
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
        _common.setProperty("class.1.region.2.center", "-5,3");
        _common.setProperty("class.1.region.2.rotation", "1, 2, -45");
        _common.setProperty("class.1.region.3.center", "0,-5");

        _common.setProperty("class.2.regions", "1");
        _common.setProperty("class.2.region.1.shape", "I");
        _common.setProperty("class.2.region.1.center", "0, 0");
        _common.setProperty("class.2.region.1.radius", "10, 10");

        _common.setProperty("examples", "1500");
        _common.setProperty("fileName", "test.arff");
        
    }
    
    @Before
    public void setUp() {
        _settings = new GeneratorSettings();
    }
    
    @Test
    public void testDefaultLearnTestRatio() {
        try {
            _settings.read(_common);
            assertThat(_settings.getLearnTestRatio(), is(new Ratio(1.0, 0.0)));
            assertThat(_settings.getNumLearnTestPairs(), is(0));
            assertThat(_settings.getFileName(), is("test.arff"));
            assertThat(_settings.getLearnFileName(), is("test.arff"));
        } catch (ConfigurationException ex) {
            Logger.getLogger(GeneratorSettingsTest_SettingsExtraction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testDefaultExampleTypeLabels() {
        try {
            _settings.read(_common);
            assertThat(_settings.getLabeledClassIndexes(), is(new ArrayList<Integer>()));
        } catch (ConfigurationException ex) {
            Logger.getLogger(GeneratorSettingsTest_SettingsExtraction.class.getName()).log(Level.SEVERE, null, ex);            
        }
    }

    @Test
    public void testExampleTypeLabelsForAllClasses() {
        try {
            _common.setProperty("exampleTypeLabels.classes", "*");
            _settings.read(_common);
            assertThat(_settings.getLabeledClassIndexes(), is(Arrays.asList(0, 1)));
        } catch (ConfigurationException ex) {
            Logger.getLogger(GeneratorSettingsTest_SettingsExtraction.class.getName()).log(Level.SEVERE, null, ex);            
        }
    }

    @Test
    public void testExampleTypeLabelsForSelectedClasses() {
        try {
            _common.setProperty("exampleTypeLabels.classes", "2");
            _settings.read(_common);
            assertThat(_settings.getLabeledClassIndexes(), is(Arrays.asList(1)));
        } catch (ConfigurationException ex) {
            Logger.getLogger(GeneratorSettingsTest_SettingsExtraction.class.getName()).log(Level.SEVERE, null, ex);            
        }
    }
    
    @Test
    public void testLearnTestRatioPairsFileNames() {
        final String FILENAME_LEARN = "learn%d.arff";
        final String FILENAME_TEST = "test%d.arff";
        
        _common.setProperty("learnTestRatio", "9:1");
        _common.setProperty("learnTestPairs", "10");
        _common.setProperty("fileName.learn", FILENAME_LEARN);
        _common.setProperty("fileName.test", FILENAME_TEST);
        try {
            _settings.read(_common);
            assertThat(_settings.getLearnTestRatio(), is(new Ratio(9.0, 1.0)));
            assertThat(_settings.getNumLearnTestPairs(), is(10));
            assertThat(_settings.getLearnFileName(), is(FILENAME_LEARN));
            assertThat(_settings.getTestFileName(), is(FILENAME_TEST));
            assertThat(_settings.getFileName(Ratio.LEARN), is(FILENAME_LEARN));
            assertThat(_settings.getFileName(Ratio.TEST), is(FILENAME_TEST));
            
        } catch (ConfigurationException ex) {
            Logger.getLogger(GeneratorSettingsTest_SettingsExtraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test(expected = ConfigurationException.class)
    public void whenInvalidClassIndexInExampleTypeLabelsClasses_thenException() throws ConfigurationException {
            _common.setProperty("exampleTypeLabels.classes", "3");
            _settings.read(_common);
    }

    @Test(expected = ConfigurationException.class)
    public void whenInvalidLearnTestRatio_thenException() throws ConfigurationException {
            _common.setProperty("learnTestRatio", "0:1");
            _settings.read(_common);
    }

    @Test(expected = ConfigurationException.class)
    public void whenInvalidCombinationOfLearnTestRatioAndLearnTestPairs_thenException() throws ConfigurationException {
            _common.setProperty("learnTestRatio", "1:0");
            _common.setProperty("learnTestPairs","5");
            _settings.read(_common);
    }

}

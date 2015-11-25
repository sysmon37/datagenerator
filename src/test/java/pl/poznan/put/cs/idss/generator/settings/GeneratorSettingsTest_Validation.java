package pl.poznan.put.cs.idss.generator.settings;

import pl.poznan.put.cs.idss.generator.settings.GeneratorSettings;
import java.util.Properties;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.MapConfiguration;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author swilk
 */
public class GeneratorSettingsTest_Validation {
    private GeneratorSettings _settings = null;
    private final Properties _changed = new Properties();
    private final Properties _common = new Properties();
        
    public GeneratorSettingsTest_Validation() {
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
    
    @Test(expected = ConfigurationException.class)
    public void whenNoBorderZone_thenException() throws ConfigurationException {
        _changed.setProperty("class.1.exampleTypeRatio", "70:10:20:0");
        _changed.setProperty("class.1.region.2.borderZone", "0");
        
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(_changed));
        config.addConfiguration(new MapConfiguration(_common));
        
        _settings.read(config);
    }

    @Test(expected = ConfigurationException.class)
    public void whenRotationsForIntegumental_thenException() throws ConfigurationException {
        _changed.setProperty("class.2.region.1.rotation", "1, 2, 30");
        
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(_changed));
        config.addConfiguration(new MapConfiguration(_common));
        
        _settings.read(config);
    }
    
    @Test(expected = ConfigurationException.class)
    public void whenMixedIntegumentalAndNonIntegumental_thenException() throws ConfigurationException {
        _changed.setProperty("class.2.regions", "2");
        _changed.setProperty("class.2.region.2.center", "4, 4");
        
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(_changed));
        config.addConfiguration(new MapConfiguration(_common));
        
        _settings.read(config);
    }
    

    
}

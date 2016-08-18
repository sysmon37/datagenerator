package pl.poznan.put.cs.idss.generator.settings;

import java.util.Arrays;
import java.util.Properties;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.MapConfiguration;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author swilk
 */
public class GeneratorSettingsTest_RegionCreation {

    private final GeneratorSettings _settings = new GeneratorSettings();
    private final Properties _common = new Properties();
    private MapConfiguration _commonConfig = null;

    public GeneratorSettingsTest_RegionCreation() {
    }
    
    @Before
    public void setUp() {
        _common.setProperty("attributes", "2");
        _common.setProperty("classes", "2");
        _common.setProperty("classRatio", "1:5");
        _common.setProperty("examples", "1000");
        _common.setProperty("learnTestRatio", "1:0");
        _common.setProperty("minOutlierDistance", "1");
        _common.setProperty("defaultRegion.distribution", "U");
        _common.setProperty("defaultRegion.shape", "C");
        _common.setProperty("defaultRegion.radius", "2, 1");        
        _common.setProperty("defaultClass.exampleTypeRatio", "100:0:0:0");
        _common.setProperty("class.1.regions", "2");
        _common.setProperty("class.1.region.1.center", "5, 5");
        _common.setProperty("class.1.region.1.rotation", "1, 2, 45");
        _common.setProperty("class.1.region.2.center", "-5, 3");
        _common.setProperty("class.1.region.2.rotation", "1, 2, -45");
        _common.setProperty("class.2.regions", "1");
        _common.setProperty("class.2.region.1.shape", "I");
        _common.setProperty("class.2.region.1.center", "0, 0");
        _common.setProperty("class.2.region.1.radius", "10, 10");
        _common.setProperty("fileName", "paw3-2d.arff");
        
        _commonConfig = new MapConfiguration(_common);
    }
    
    @After
    public void tearDown() {
    }

    @Test(expected = ConfigurationException.class)
    public void whenWeightIsMissing_thenThrowsException() throws ConfigurationException {
        _settings.read(_commonConfig);
    }
    
    @Test(expected = ConfigurationException.class)
    public void whenBorderZoneIsMissing_thenThrowsException() throws ConfigurationException {
        Properties changed = new Properties();
        changed.setProperty("defaultRegion.weight", "1.0");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(changed));
        config.addConfiguration(_commonConfig);
        _settings.read(_commonConfig);
    }

    @Test(expected = ConfigurationException.class)
    public void whenNoOitlierZoneIsMissing_thenThrowsException() throws ConfigurationException {
        Properties changed = new Properties();
        changed.setProperty("defaultRegion.weight", "2.0");
        changed.setProperty("defaultRegion.borderZone", "1.0");
        
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(changed));
        config.addConfiguration(_commonConfig);
            
        _settings.read(config);
    }
    
    @Test
    public void whenAutoBorderAndBorderZoneInDefaultRegion_thenReadAndStoreBorderZone() throws ConfigurationException {
        Properties changed = new Properties();
        changed.setProperty("defaultRegion.weight", "2.0");
        changed.setProperty("defaultRegion.borderZone", "1.0");
        changed.setProperty("defaultRegion.noOutlierZone", "1.0");
        changed.setProperty("defaultRegion.border", "auto");
        changed.setProperty("class.1.region.1.border", "fixed");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(changed));
        config.addConfiguration(_commonConfig);
            
        _settings.read(config);
        
        Size region1_safeRadius = new Size(Arrays.asList(2.0, 1.0));
        Size region1_borderRadius = new Size(Arrays.asList(3.0, 2.0));
        Size region1_noOutlierRadius = new Size(Arrays.asList(4.0, 3.0));

        Size region2_safeRadius = new Size(Arrays.asList(2.0, 1.0));
        Size region2_borderRadius = new Size(region2_safeRadius);
        Size region2_noOutlierRadius = new Size(Arrays.asList(3.0, 2.0));
        
        Region region1 = _settings.getDecisionClass(0).getRegion(0);
        assertThat(region1.getSafeRadius(), is(region1_safeRadius));
        assertThat(region1.getBorderRadius(), is(region1_borderRadius));
        assertThat(region1.getNoOutlierRadius(), is(region1_noOutlierRadius));

        Region region2 = _settings.getDecisionClass(0).getRegion(1);
        assertThat(region2.getSafeRadius(), is(region2_safeRadius));
        assertThat(region2.getBorderRadius(), is(region2_borderRadius));
        assertThat(region2.getNoOutlierRadius(), is(region2_noOutlierRadius));

    }
    
}

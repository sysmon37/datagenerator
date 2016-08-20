package pl.poznan.put.cs.idss.generator.settings;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.MapConfiguration;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;

/**
 *
 * @author swilk
 */
public class GeneratorSettingsTest_AutoBorder_3D {

    private final GeneratorSettings _settings = new GeneratorSettings();
    private final Properties _common = new Properties();
    private MapConfiguration _commonConfig = null;
    
    private final Size region2_radius = new Size(Arrays.asList(2.0, 4.0, 3.0));
    private final Size region2_safeRadius = new Size(region2_radius);
    private final Size region2_borderRadius = new Size(Arrays.asList(4.0, 6.0, 5.0));
    private final Size region2_noOutlierRadius = new Size(Arrays.asList(5.0, 7.0, 6.0));
    
    static final double RADIUS_X = 6.0;
    static final double RADIUS_Y = 3.0;
    static final double RADIUS_Z = 4.0;
    static final double NO_OUTLIER_ZONE = 1.0;

    
    public GeneratorSettingsTest_AutoBorder_3D() {
    }
    
    @Before
    public void setUp() {
        _common.setProperty("attributes", "3");
        _common.setProperty("classes", "2");
        _common.setProperty("classRatio", "1:5");
        _common.setProperty("examples", "100");
        _common.setProperty("learnTestRatio", "1:0");
        _common.setProperty("minOutlierDistance", "1");
        _common.setProperty("defaultRegion.distribution", "U");
        _common.setProperty("defaultRegion.shape", "C");
        _common.setProperty("defaultRegion.radius", "2, 1, 3");
        _common.setProperty("defaultRegion.border", "fixed");
        _common.setProperty("defaultRegion.borderZone", "2");
        _common.setProperty("defaultRegion.noOutlierZone", String.format(Locale.US, "%f", NO_OUTLIER_ZONE));

        _common.setProperty("defaultClass.exampleTypeRatio", "100:0:0:0");
        _common.setProperty("class.1.regions", "2");
        _common.setProperty("class.1.region.1.center", "6, 3, 4");
        _common.setProperty("class.1.region.1.radius", String.format(Locale.US, "%f, %f, %f", RADIUS_X, RADIUS_Y, RADIUS_Z));
        _common.setProperty("class.1.region.1.border", "auto");        
        _common.setProperty("class.1.region.2.center", "-8, -5, -3");
        _common.setProperty("class.1.region.2.radius", "2, 4, 3");
        _common.setProperty("class.2.regions", "1");
        _common.setProperty("class.2.region.1.shape", "I");
        _common.setProperty("class.2.region.1.center", "0, 0, 0");
        _common.setProperty("class.2.region.1.radius", "12, 12, 12");
        _common.setProperty("fileName", "test-3d.arff");
        
        _commonConfig = new MapConfiguration(_common);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void ditributeExamples_100_0_0_0() throws ConfigurationException {
        double safeBorderScaling = 1.0;
        double safeScaling = 1.0;
        
        Size region1_radius = new Size(Arrays.asList(RADIUS_X, RADIUS_Y, RADIUS_Z));
        Size region1_safeRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling*safeScaling, RADIUS_Y*safeBorderScaling*safeScaling, RADIUS_Z*safeBorderScaling*safeScaling));
        Size region1_borderRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling, RADIUS_Y*safeBorderScaling, RADIUS_Z*safeBorderScaling));
        Size region1_noOutlierRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Y*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Z*safeBorderScaling + NO_OUTLIER_ZONE));

        Properties changed = new Properties();
        changed.setProperty("class.1.exampleTypeRatio", "100:0:0:0");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(changed));
        config.addConfiguration(_commonConfig);
        _settings.read(config);
        
        Region region1 = _settings.getDecisionClass(0).getRegion(0);
        assertThat(region1.getRadius(), is(region1_radius));
        assertThat(region1.getSafeRadius(), is(region1_safeRadius));
        assertThat(region1.getBorderRadius(), is(region1_borderRadius));
        assertThat(region1.getNoOutlierRadius(), is(region1_noOutlierRadius));

        Region region2 = _settings.getDecisionClass(0).getRegion(1);
        assertThat(region2.getRadius(), is(region2_radius));
        assertThat(region2.getSafeRadius(), is(region2_safeRadius));
        assertThat(region2.getBorderRadius(), is(region2_borderRadius));
        assertThat(region2.getNoOutlierRadius(), is(region2_noOutlierRadius));       
    }
    
    @Test
    public void ditributeExamples_80_0_10_10() throws ConfigurationException {
        double safeBorderScaling = Math.pow(0.8, 1.0/3.0);
        double safeScaling = Math.pow(1.0, 1.0/3.0);
        
        Size region1_radius = new Size(Arrays.asList(RADIUS_X, RADIUS_Y, RADIUS_Z));
        Size region1_safeRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling*safeScaling, RADIUS_Y*safeBorderScaling*safeScaling, RADIUS_Z*safeBorderScaling*safeScaling));
        Size region1_borderRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling, RADIUS_Y*safeBorderScaling, RADIUS_Z*safeBorderScaling));
        Size region1_noOutlierRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Y*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Z*safeBorderScaling + NO_OUTLIER_ZONE));

        Properties changed = new Properties();
        changed.setProperty("class.1.exampleTypeRatio", "80:0:10:10");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(changed));
        config.addConfiguration(_commonConfig);
        _settings.read(config);
        
        Region region1 = _settings.getDecisionClass(0).getRegion(0);
        assertThat(region1.getRadius(), is(region1_radius));
        assertThat(region1.getSafeRadius(), is(region1_safeRadius));
        assertThat(region1.getBorderRadius(), is(region1_borderRadius));
        assertThat(region1.getNoOutlierRadius(), is(region1_noOutlierRadius));

        Region region2 = _settings.getDecisionClass(0).getRegion(1);
        assertThat(region2.getRadius(), is(region2_radius));
        assertThat(region2.getSafeRadius(), is(region2_safeRadius));
        assertThat(region2.getBorderRadius(), is(region2_borderRadius));
        assertThat(region2.getNoOutlierRadius(), is(region2_noOutlierRadius));        
    }

    @Test
    public void ditributeExamples_80_20_0_0() throws ConfigurationException {
        double safeBorderScaling = Math.pow(1.0, 1.0/3.0);
        double safeScaling = Math.pow(0.8, 1.0/3.0);
        
        Size region1_radius = new Size(Arrays.asList(RADIUS_X, RADIUS_Y, RADIUS_Z));
        Size region1_safeRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling*safeScaling, RADIUS_Y*safeBorderScaling*safeScaling, RADIUS_Z*safeBorderScaling*safeScaling));
        Size region1_borderRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling, RADIUS_Y*safeBorderScaling, RADIUS_Z*safeBorderScaling));
        Size region1_noOutlierRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Y*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Z*safeBorderScaling + NO_OUTLIER_ZONE));

        Properties changed = new Properties();
        changed.setProperty("class.1.exampleTypeRatio", "80:20:0:0");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(changed));
        config.addConfiguration(_commonConfig);
        _settings.read(config);
        
        Region region1 = _settings.getDecisionClass(0).getRegion(0);
        assertThat(region1.getRadius(), is(region1_radius));
        assertThat(region1.getSafeRadius(), is(region1_safeRadius));
        assertThat(region1.getBorderRadius(), is(region1_borderRadius));
        assertThat(region1.getNoOutlierRadius(), is(region1_noOutlierRadius));

        Region region2 = _settings.getDecisionClass(0).getRegion(1);
        assertThat(region2.getRadius(), is(region2_radius));
        assertThat(region2.getSafeRadius(), is(region2_safeRadius));
        assertThat(region2.getBorderRadius(), is(region2_borderRadius));
        assertThat(region2.getNoOutlierRadius(), is(region2_noOutlierRadius));        
    }

    @Test
    public void ditributeExamples_50_50_0_0() throws ConfigurationException {
        double safeBorderScaling = Math.pow(1.0, 1.0/3.0);
        double safeScaling = Math.pow(0.5, 1.0/3.0);
        
        Size region1_radius = new Size(Arrays.asList(RADIUS_X, RADIUS_Y, RADIUS_Z));
        Size region1_safeRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling*safeScaling, RADIUS_Y*safeBorderScaling*safeScaling, RADIUS_Z*safeBorderScaling*safeScaling));
        Size region1_borderRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling, RADIUS_Y*safeBorderScaling, RADIUS_Z*safeBorderScaling));
        Size region1_noOutlierRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Y*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Z*safeBorderScaling + NO_OUTLIER_ZONE));

        Properties changed = new Properties();
        changed.setProperty("class.1.exampleTypeRatio", "50:50:0:0");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(changed));
        config.addConfiguration(_commonConfig);
        _settings.read(config);
        
        Region region1 = _settings.getDecisionClass(0).getRegion(0);
        assertThat(region1.getRadius(), is(region1_radius));
        assertThat(region1.getSafeRadius(), is(region1_safeRadius));
        assertThat(region1.getBorderRadius(), is(region1_borderRadius));
        assertThat(region1.getNoOutlierRadius(), is(region1_noOutlierRadius));

        Region region2 = _settings.getDecisionClass(0).getRegion(1);
        assertThat(region2.getRadius(), is(region2_radius));
        assertThat(region2.getSafeRadius(), is(region2_safeRadius));
        assertThat(region2.getBorderRadius(), is(region2_borderRadius));
        assertThat(region2.getNoOutlierRadius(), is(region2_noOutlierRadius));        
    }
    
    @Test
    public void ditributeExamples_40_40_10_10() throws ConfigurationException {
        double safeBorderScaling = Math.pow(0.8, 1.0/3.0);
        double safeScaling = Math.pow(0.5, 1.0/3.0);
        
        Size region1_radius = new Size(Arrays.asList(RADIUS_X, RADIUS_Y, RADIUS_Z));
        Size region1_safeRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling*safeScaling, RADIUS_Y*safeBorderScaling*safeScaling, RADIUS_Z*safeBorderScaling*safeScaling));
        Size region1_borderRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling, RADIUS_Y*safeBorderScaling, RADIUS_Z*safeBorderScaling));
        Size region1_noOutlierRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Y*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Z*safeBorderScaling + NO_OUTLIER_ZONE));

        Properties changed = new Properties();
        changed.setProperty("class.1.exampleTypeRatio", "40:40:10:10");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(changed));
        config.addConfiguration(_commonConfig);
        _settings.read(config);
        
        Region region1 = _settings.getDecisionClass(0).getRegion(0);
        assertThat(region1.getRadius(), is(region1_radius));
        assertThat(region1.getSafeRadius(), is(region1_safeRadius));
        assertThat(region1.getBorderRadius(), is(region1_borderRadius));
        assertThat(region1.getNoOutlierRadius(), is(region1_noOutlierRadius));

        Region region2 = _settings.getDecisionClass(0).getRegion(1);
        assertThat(region2.getRadius(), is(region2_radius));
        assertThat(region2.getSafeRadius(), is(region2_safeRadius));
        assertThat(region2.getBorderRadius(), is(region2_borderRadius));
        assertThat(region2.getNoOutlierRadius(), is(region2_noOutlierRadius));        
    }

    @Test
    public void ditributeExamples_0_80_10_10() throws ConfigurationException {
        double safeBorderScaling = Math.pow(0.8, 1.0/3.0);
        double safeScaling = Math.pow(0.0, 1.0/3.0);
        
        Size region1_radius = new Size(Arrays.asList(RADIUS_X, RADIUS_Y, RADIUS_Z));
        Size region1_safeRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling*safeScaling, RADIUS_Y*safeBorderScaling*safeScaling, RADIUS_Z*safeBorderScaling*safeScaling));
        Size region1_borderRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling, RADIUS_Y*safeBorderScaling, RADIUS_Z*safeBorderScaling));
        Size region1_noOutlierRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Y*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Z*safeBorderScaling + NO_OUTLIER_ZONE));

        Properties changed = new Properties();
        changed.setProperty("class.1.exampleTypeRatio", "0:80:10:10");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(changed));
        config.addConfiguration(_commonConfig);
        _settings.read(config);
        
        Region region1 = _settings.getDecisionClass(0).getRegion(0);
        assertThat(region1.getRadius(), is(region1_radius));
        assertThat(region1.getSafeRadius(), is(region1_safeRadius));
        assertThat(region1.getBorderRadius(), is(region1_borderRadius));
        assertThat(region1.getNoOutlierRadius(), is(region1_noOutlierRadius));

        Region region2 = _settings.getDecisionClass(0).getRegion(1);
        assertThat(region2.getRadius(), is(region2_radius));
        assertThat(region2.getSafeRadius(), is(region2_safeRadius));
        assertThat(region2.getBorderRadius(), is(region2_borderRadius));
        assertThat(region2.getNoOutlierRadius(), is(region2_noOutlierRadius));        
    }

    @Test
    public void ditributeExamples_0_0_50_50() throws ConfigurationException {
        double safeBorderScaling = Math.pow(0.0, 1.0/3.0);
        double safeScaling = Math.pow(0.0, 1.0/3.0);
        
        Size region1_radius = new Size(Arrays.asList(RADIUS_X, RADIUS_Y, RADIUS_Z));
        Size region1_safeRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling*safeScaling, RADIUS_Y*safeBorderScaling*safeScaling, RADIUS_Z*safeBorderScaling*safeScaling));
        Size region1_borderRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling, RADIUS_Y*safeBorderScaling, RADIUS_Z*safeBorderScaling));
        Size region1_noOutlierRadius = new Size(Arrays.asList(RADIUS_X*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Y*safeBorderScaling + NO_OUTLIER_ZONE, RADIUS_Z*safeBorderScaling + NO_OUTLIER_ZONE));

        Properties changed = new Properties();
        changed.setProperty("class.1.exampleTypeRatio", "0:0:50:50");

        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new MapConfiguration(changed));
        config.addConfiguration(_commonConfig);
        _settings.read(config);
        
        Region region1 = _settings.getDecisionClass(0).getRegion(0);
        assertThat(region1.getRadius(), is(region1_radius));
        assertThat(region1.getSafeRadius(), is(region1_safeRadius));
        assertThat(region1.getBorderRadius(), is(region1_borderRadius));
        assertThat(region1.getNoOutlierRadius(), is(region1_noOutlierRadius));

        Region region2 = _settings.getDecisionClass(0).getRegion(1);
        assertThat(region2.getRadius(), is(region2_radius));
        assertThat(region2.getSafeRadius(), is(region2_safeRadius));
        assertThat(region2.getBorderRadius(), is(region2_borderRadius));
        assertThat(region2.getNoOutlierRadius(), is(region2_noOutlierRadius));        
    }
    
}

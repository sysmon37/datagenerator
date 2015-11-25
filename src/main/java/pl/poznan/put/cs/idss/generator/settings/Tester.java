package pl.poznan.put.cs.idss.generator.settings;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.commons.configuration.ConfigurationException;

/**
 *
 * @author swilk
 */
public class Tester {

    public static void main(String[] args) throws ConfigurationException {
        if (args.length == 0)
            return;
        String configFileName = args[0];
        
        List<String> exampleTypeRatioStrings = Arrays.asList("90:10:0:0", "70:30:0:0", "60:30:0:10",
                "60:20:10:10", "50:30:10:10", "40:40:10:10", "30:50:10:10", "20:60:10:10", "10:60:20:10",
                "10:50:20:20", "10:40:20:30", "10:30:30:30", "0:40:30:30", "0:30:30:40", "0:20:30:50",
                "0:10:30:60", "0:0:30:70", "0:0:10:90");
        
        for (String ratioString : exampleTypeRatioStrings) {
            Properties properties = new Properties();
            properties.setProperty("class.1.exampleTypeRatio", ratioString);
            GeneratorSettings settings = new GeneratorSettings();
            settings.read(configFileName, properties);            
        }            
    }
}

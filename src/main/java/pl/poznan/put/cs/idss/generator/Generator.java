package pl.poznan.put.cs.idss.generator;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import pl.poznan.put.cs.idss.generator.settings.GeneratorSettings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.Validate;
import pl.poznan.put.cs.idss.generator.factories.DataSetGeneratorFactory;
import pl.poznan.put.cs.idss.generator.generation.DataSetGenerator;
import pl.poznan.put.cs.idss.generator.generation.Example;
import pl.poznan.put.cs.idss.generator.settings.ParameterExtractor;
import pl.poznan.put.cs.idss.generator.settings.Ratio;

/**
 * Main data generator class
 */

@Getter @Slf4j
public class Generator {
    
    private final static String OPTION_CONFIG = "config";
    private final static String OPTION_HELP = "help";
    private final static String OPTION_PROPERTY = "D";

    private ARFFWriter _writer = null;
    private GeneratorSettings _settings = null;
    
    public Generator(GeneratorSettings settings) {
        Validate.notNull(settings);
        _settings = settings;
        _writer = new ARFFWriter(settings);
    }
    
    public void generate() throws IOException {
        ParameterExtractor extractor = new ParameterExtractor(_settings);
           
        DataSetGenerator dataSetGenerator = DataSetGeneratorFactory.create(
                extractor.getMinOutlierDistance(),
                extractor.getRegionDescriptions(),
                extractor.getOutlierDescriptions());     
        
        int numPasses = Math.max(1, _settings.getNumLearnTestPairs());
        Ratio learnTestRatio = _settings.getLearnTestRatio();
        
        for (int p = 0; p < numPasses; p++) {
            
            for (int setIndex : Ratio.SET_INDEXES) 
                if (learnTestRatio.get(setIndex) > 0.0) {
                    List<Example> examples = dataSetGenerator.generateExamples(setIndex);
                    processExamples(examples, setIndex, p);
                }
        }                       
    }
    
    protected void processExamples(List<Example> examples, int setIndex, int pass) throws IOException {
        _writer.writeToFile(String.format(_settings.getFileName(setIndex), pass + 1), examples);
    }
    
    public static void main(String[] args) throws Exception {

        Options options = new Options();
        options.addOption(OPTION_HELP, false, "show help");
        options.addOption(OPTION_CONFIG, true, "configuration file");
        Option propertiesOption = Option.builder(OPTION_PROPERTY).argName("property=value").numberOfArgs(2).valueSeparator().build();
        options.addOption(propertiesOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption(OPTION_HELP)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("generator", options);
                return;
            }
            
            GeneratorSettings settings = new GeneratorSettings();
            Properties properties = cmd.getOptionProperties("D");
            if (cmd.hasOption(OPTION_CONFIG))
                settings.read(cmd.getOptionValue(OPTION_CONFIG), properties);
            else {
                Validate.validState(!properties.isEmpty(), "No properties have been provided in command line using -D option");
                settings.read(properties);
            }
            Generator generator = new Generator(settings);
            generator.generate();
        } catch (ParseException ex) {
            System.err.printf("Parsing failed. Reason: %s\n", ex.getMessage());
        } catch (ConfigurationException | IOException | IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            throw ex;
        }
    }

}

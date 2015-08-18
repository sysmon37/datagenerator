package cli;

import java.io.IOException;
import java.util.List;

import factories.DataSetFactory;
import factories.OutlierDescription;
import generator.DataSet;
import generator.Instance;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    private final static String OPTION_CONFIG = "config";
    private final static String OPTION_ADD_LABELS = "addLabels";
    private final static String OPTION_HELP = "help";

    public static void main(String[] args) throws IOException {

        Options options = new Options();
        options.addOption(OPTION_HELP, false, "show help");
        options.addOption(OPTION_CONFIG, true, "configuration file");
        options.addOption(OPTION_ADD_LABELS, true, "add labels of examples from a selected class");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption(OPTION_HELP)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("generator", options);
                return;
            }
            if (!cmd.hasOption(OPTION_CONFIG)) {
                throw new ParseException(String.format("The %s option has not been provided", OPTION_CONFIG));
            }
            
            String path = cmd.getOptionValue(OPTION_CONFIG);
            InputStreamReader reader = new InputStreamReader(path);
            ParameterExtractor extractor = new ParameterExtractor();
            extractor.add(reader.readAll());

            if (cmd.hasOption(OPTION_ADD_LABELS)) {
                extractor.setAddLabels(true);
                extractor.setLabeledClassIndex(cmd.getOptionValue(OPTION_ADD_LABELS));
            }
            
            for (int i = 0; i < extractor.getNumberOfTrainingTestPairsToBeGenerated(); ++i) {
                List<OutlierDescription> outlierDescriptions = extractor.getOutliers();
                DataSet dataSet = DataSetFactory.create(extractor.getInterOutlierDistance(),
                        extractor.getCommands(),
                        outlierDescriptions);
                List<Instance>[] instances = generateSets(dataSet);

                DataSetDescriptionCreator descriptionCreator = new DataSetDescriptionCreator(extractor,
                        outlierDescriptions);
                String dataSetDescription = descriptionCreator.getDescription();
                System.out.println(dataSetDescription);
                String filename = removeExtension(path);
                ArffWriter.writeToFile(extractor, filename + "_training_" + i + ".arff", instances[0], dataSetDescription);
                ArffWriter.writeToFile(extractor, filename + "_test_" + i + ".arff", instances[1], dataSetDescription);
            }

        } catch (ParseException ex) {
            System.err.printf("Parsing failed. Reason: %s\n", ex.getMessage());
        } catch (IOException ex) {
            System.err.printf("File operation failed. Reason: %s\n", ex.getMessage());
        }
    }

    private static String removeExtension(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex >= 0) {
            return filename.substring(0, lastIndex);
        }
        return filename;
    }

    private static List<Instance>[] generateSets(DataSet dataSet) {
        @SuppressWarnings("unchecked")
        List<Instance>[] instances = new List[11];
        instances[0] = dataSet.generateTrainingSet();
        instances[1] = dataSet.generateTestSet();
        return instances;
    }
}

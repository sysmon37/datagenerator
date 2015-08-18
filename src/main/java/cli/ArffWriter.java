package cli;

import generator.Instance;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ArffWriter {

    
    public static void writeToFile(ParameterExtractor config, String filename,
            List<Instance> instances,
            String dataSetDescription) throws IOException {
        FileWriter writer = new FileWriter(filename);

        // description
        writer.write(prepareDescription(dataSetDescription) + "\n\n\n");

        // header
        writer.write("@relation GENERATED\n");
        writer.write(prepareAttributes(config));
        writer.write(prepareClasses(config));
        writer.write(prepareLabels(config));
        
        // body
        writer.write("@data\n");
        for (Instance instance : instances) {
            writer.write(instance.toString(config.isAddLabels(), config.getLabeledClassIndex()) + '\n');
        }

        writer.close();
    }

    

    private static String prepareClasses(ParameterExtractor config) {
        StringBuilder sb = new StringBuilder("@attribute CLASS {");
        for (int i = 0; i < config.getNumberOfClasses(); i++)
            sb.append(i > 0 ? ", " : ""). append(i);
        sb.append("}\n");
        return sb.toString();
    }

        
    private static String prepareAttributes(ParameterExtractor config) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < config.getNumberOfAttributes(); i++)
            sb.append(String.format("@attribute ATTR_%d numeric\n", i + 1));
        return sb.toString();
    }

    private static String prepareDescription(String description) {
        StringBuilder result = new StringBuilder();
        for (String s : description.split("\n")) {
            result.append("% ").append(s).append("\n");
        }
        return result.toString();
    }

    private static String prepareLabels(ParameterExtractor config) {
        if (config.isAddLabels()) {
            StringBuilder sb = new StringBuilder("@attribute LABEL {");
            sb.append(StringUtils.join(Instance.Label.values(), ", ")).append(("}\n"));
            return sb.toString();
        } else
            return StringUtils.EMPTY;
    }
}

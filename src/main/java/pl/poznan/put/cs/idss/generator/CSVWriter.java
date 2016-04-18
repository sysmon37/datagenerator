package pl.poznan.put.cs.idss.generator;

import pl.poznan.put.cs.idss.generator.settings.GeneratorSettings;
import pl.poznan.put.cs.idss.generator.generation.Example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVWriter extends DataSetWriter {

    public CSVWriter(GeneratorSettings settings) {
        super(settings);
    }

    @Override
    public void writeToFile(String fileName,
            List<Example> examples) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            log.info("Saving CSV file {}...", fileName);
            boolean anyLabeledClasses = !getSettings().getLabeledClassIndexes().isEmpty();

            writer.write(prepareHeader(anyLabeledClasses) + "\n");

            for (Example example : examples) {
                String className = getSettings().getClassName(example.getClassIndex());
                String exampleString;
                if (!anyLabeledClasses || getSettings().getLabeledClassIndexes().indexOf(example.getClassIndex()) == -1) {
                    exampleString = example.toString(className);
                } else {
                    exampleString = example.toString(String.format("%s-%s", className, example.getLabel().toString()));
                }
                writer.write(exampleString + "\n");
            }
        }
    }

    private String prepareHeader(boolean anyLabeledClasses) {
        StringBuilder sb = new StringBuilder();
        for (int a = 0; a < getSettings().getNumAttributes(); a++) {
            if (a > 0) {
                sb.append(",");
            }
            sb.append(getSettings().getAttributeName(a));
        }

        if (!anyLabeledClasses) {
            sb.append(",");
            sb.append(getSettings().getDecisionName());
        } else {
            sb.append(",LABEL");
        }

        return sb.toString();
    }
}

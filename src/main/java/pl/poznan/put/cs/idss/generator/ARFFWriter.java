package pl.poznan.put.cs.idss.generator;

import pl.poznan.put.cs.idss.generator.settings.GeneratorSettings;
import pl.poznan.put.cs.idss.generator.generation.Example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ARFFWriter {

    private GeneratorSettings _settings = null;
    
    public ARFFWriter(GeneratorSettings settings) {
        Validate.notNull(settings);
        _settings = settings;
    }
    
    public void writeToFile(String fileName,
            List<Example> examples) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            // description
//            if (!description.isEmpty())
//                writer.write(prepareDescription(description) + "\n");
            
            log.info("Saving file {}...", fileName);
            boolean anyLabeledClasses = !_settings.getLabeledClassIndexes().isEmpty();
            // header (relation)
            writer.write("@relation GENERATED\n");
            writer.write(prepareAttributes() + "\n");
            if (anyLabeledClasses)
                writer.write(prepareLabels() + "\n");
            else
                writer.write(prepareClasses() + "\n");
            
            // body (data)
            writer.write("@data\n");
            for (Example example : examples) {
                String className = _settings.getClassName(example.getClassIndex());
                String exampleString;
                if (!anyLabeledClasses || _settings.getLabeledClassIndexes().indexOf(example.getClassIndex()) == -1)
                    exampleString = example.toString(className);
                else 
                    exampleString = example.toString(String.format("%s-%s", className, example.getLabel().toString()));
                writer.write(exampleString + "\n");
            }
        }
    }

    

    private String prepareClasses() {
        StringBuilder sb = new StringBuilder(String.format("@attribute %s {", _settings.getDecisionName()));
        sb.append(StringUtils.join(_settings.getClassNames(), ", ")).append("}");
        return sb.toString();
    }

        
    private String prepareAttributes() {
        StringBuilder sb = new StringBuilder();
        for (int a = 0; a < _settings.getNumAttributes(); a++) {
            if (a > 0)
                sb.append("\n");
            sb.append(String.format("@attribute %s numeric", _settings.getAttributeName(a)));
        }
        return sb.toString();
    }

    private String prepareDescription(String description) {
        StringBuilder result = new StringBuilder();
        for (String s : description.split("\n")) {
            result.append("% ").append(s).append("\n");
        }
        return result.toString();
    }

    private String prepareLabels() {
        List<String> allLabels = new ArrayList<>();
        for (int c = 0; c < _settings.getNumClasses(); c++) {
            String className = _settings.getClassName(c);
            if (_settings.getLabeledClassIndexes().indexOf(c) == -1)
                allLabels.add(className);
            else {
                for (Example.Label l : Example.Label.values())
                    allLabels.add(String.format("%s-%s", className, l.toString()));
            }
        }
        StringBuilder sb = new StringBuilder("@attribute LABEL {");
        sb.append(StringUtils.join(allLabels, ", ")).append(("}\n"));
        return sb.toString();            
    }        
}

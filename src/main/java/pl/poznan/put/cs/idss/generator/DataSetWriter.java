package pl.poznan.put.cs.idss.generator;

import java.io.IOException;
import pl.poznan.put.cs.idss.generator.settings.GeneratorSettings;
import pl.poznan.put.cs.idss.generator.generation.Example;

import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;

@Getter
@Slf4j
public abstract class DataSetWriter {

    private GeneratorSettings _settings = null;
    
    public DataSetWriter(GeneratorSettings settings) {
        Validate.notNull(settings);
        _settings = settings;
    }
    
    public abstract void writeToFile(String fileName, List<Example> examples)
            throws IOException;
}

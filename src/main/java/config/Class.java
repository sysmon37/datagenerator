package config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author swilk
 */
@Data
public class Class {
    private List<Region> _regions = new ArrayList<>();
    private Ratio _exampleTypeRatio = null;
    private Ratio _regionRatio = null;

}

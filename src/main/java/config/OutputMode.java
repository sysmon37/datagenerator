package config;

/**
 *
 * @author swilk
 */
public enum OutputMode {
    
    SINGLE("single"), SPLIT("split");

    private final String _code;

    OutputMode(String c) {
        _code = c;
    }
    
    public static OutputMode validate(String c) {
        for (OutputMode m: values())
            if (m._code.equalsIgnoreCase(c))
                return m;
        return null;
    }
    
    public static boolean isValid(String c) {
        return validate(c) != null;
    }
    
}

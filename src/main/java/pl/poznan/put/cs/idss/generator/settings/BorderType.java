package pl.poznan.put.cs.idss.generator.settings;


/**
 *
 * @author swilk
 */
public enum BorderType {
    AUTO("auto"), FIXED("fixed");
    
    private String _code = "";
    
    BorderType(String code) {
        _code = code;
    }

    static BorderType validate(String s) {
        for (BorderType v : values())
            if (v._code.equalsIgnoreCase(s))
                return v;
         return null;
    }
        
    static boolean isValid(String s) {
        return validate(s) != null;
    }
        
    public String getCode() {
        return _code;
    }
    
}

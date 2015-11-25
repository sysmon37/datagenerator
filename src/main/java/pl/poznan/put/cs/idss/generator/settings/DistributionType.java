package pl.poznan.put.cs.idss.generator.settings;

/**
 *
 * @author swilk
 */
    public enum DistributionType {
        UNIFORM("U"), NORMAL("N");
    
        private String _code = "";
        
        DistributionType(String code) {
            _code = code;
        }

        static DistributionType validate(String s) {
            for (DistributionType v : values())
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

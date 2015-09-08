/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package config;

/**
 *
 * @author swilk
 */
    public enum ShapeType {

        CIRCLE("C"), RECTANGLE("R"), INTEGUMENTAL("I");

        private final String _code;

        ShapeType(String code) {
            _code = code;
        }

        static ShapeType validate(String s) {
            for (ShapeType v : values()) {
                if (v._code.equalsIgnoreCase(s)) {
                    return v;
                }
            }
            return null;
        }

        static boolean isValid(String s) {
            return validate(s) != null;
        }
        
        String getCode() {
            return _code;
        }
    }
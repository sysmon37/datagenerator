package pl.poznan.put.cs.idss.generator.settings;

/**
 *
 * @author swilk
 */
public enum RangeType {
    SAFE,
    /**
     * safe + border zone
     */
    BORDER,
    /**
     * border + no outlier zone
     */
    NO_OUTLIER
}

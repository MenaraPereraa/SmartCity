package lk.menara.smartcity360.dto;

/**
 * Simple label-value pair for populating HTML select elements.
 */
public class SelectOption {

    private final String value;
    private final String label;

    public SelectOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() { return value; }
    public String getLabel() { return label; }
}

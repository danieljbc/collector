package collector;

/**
 * Enumeration representing the different steps that are present
 * in processes supported by this framework.
 * Contains a label so that the name can be presented using
 * a "prettier" format.
 */
public enum ProcessSteps {
    COLLECTION("Collection"),
    PROCESSING("Processing"),
    COMMUNICATION("Communication");

    private final String label;

    ProcessSteps(final String label){
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

package abapci.feature.activeFeature;

public class PrettyPrinterFeature extends ActiveFeature {
	private String prefix;
	private boolean cleanupVariablesEnabled;

	public PrettyPrinterFeature(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setCleanupVariablesEnabled(boolean cleanupVariablesEnabled) {
		this.cleanupVariablesEnabled = cleanupVariablesEnabled;
	}

	public boolean isCleanupVariablesEnabled() {
		return cleanupVariablesEnabled;
	}

}

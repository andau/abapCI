package abapci.feature;

public class SourcecodeFormattingFeature extends ActiveFeature {
	private String prefix;
	private boolean cleanupVariablesEnabled;

	public SourcecodeFormattingFeature(String prefix) {
		this.prefix = prefix;
	}

	public SourcecodeFormattingFeature() {
		throw new UnsupportedOperationException();
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

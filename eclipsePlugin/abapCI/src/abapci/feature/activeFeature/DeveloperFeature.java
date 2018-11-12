package abapci.feature.activeFeature;

public class DeveloperFeature extends ActiveFeature {

	private boolean javaSimuModeEnabled;
	private boolean tracingEnabled;

	public boolean isJavaSimuModeEnabled() {
		return javaSimuModeEnabled;
	}

	public void setJavaSimuModeEnabled(boolean javaSimuModeEnabled) {
		this.javaSimuModeEnabled = javaSimuModeEnabled;
	}

	public boolean isTracingEnabled() {
		return tracingEnabled;
	}

	public void setTracingEnabled(boolean tracingEnabled) {
		this.tracingEnabled = tracingEnabled;
	}

}

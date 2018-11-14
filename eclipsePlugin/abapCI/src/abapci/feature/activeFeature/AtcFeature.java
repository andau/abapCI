package abapci.feature.activeFeature;

public class AtcFeature extends ActiveFeature {
	private String variant;
	private boolean runActivatedObjects;
	private boolean annotationHandlingEnabled;

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public void setRunActivatedObjects(boolean runActivatedObjects) {
		this.runActivatedObjects = runActivatedObjects;
	}

	public boolean isRunActivatedObjects() {
		return runActivatedObjects;
	}

	public boolean isAnnotationHandlingEnabled() {
		return annotationHandlingEnabled;
	}

	public void setAnnotationhandlingEnabled(boolean enabled) {
		annotationHandlingEnabled = enabled;
	}
}

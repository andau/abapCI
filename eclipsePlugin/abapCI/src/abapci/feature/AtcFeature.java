package abapci.feature;

public class AtcFeature extends ActiveFeature {
	private String variant;
	private boolean runActivatedObjects;

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
}

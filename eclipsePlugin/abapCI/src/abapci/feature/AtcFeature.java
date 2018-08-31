package abapci.feature;

public class AtcFeature extends ActiveFeature {
	private String variant;
	private boolean runActivatedObjects;
	private boolean runInitial;

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public void setRunInitial(boolean runInitial) {
		this.runInitial = runInitial;
	}

	public void setRunActivatedObjects(boolean runActivatedObjects) {
		this.runActivatedObjects = runActivatedObjects;
	}

	public boolean isRunInitial() {
		return runInitial;
	}

	public boolean isRunActivatedObjects() {
		return runActivatedObjects;
	}
}

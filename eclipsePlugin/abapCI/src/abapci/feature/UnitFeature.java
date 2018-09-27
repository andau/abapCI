package abapci.feature;

public class UnitFeature extends ActiveFeature {

	private boolean runActivatedObjectsOnly;

	public boolean isRunActivatedObjectsOnly() {
		return runActivatedObjectsOnly;
	}

	public void setRunActivatedObjectsOnly(boolean runActivatedObjectsOnly) {
		this.runActivatedObjectsOnly = runActivatedObjectsOnly;
	}

}

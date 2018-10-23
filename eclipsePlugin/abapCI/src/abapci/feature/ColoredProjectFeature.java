package abapci.feature;

public class ColoredProjectFeature {

	boolean changeStatusBarActive;
	private boolean leftRulerActive;
	private boolean rightRulerActive;
	private boolean dialogEnabled;

	public boolean isChangeStatusBarActive() {
		return changeStatusBarActive;
	}

	public void setChangeStatusBarActive(boolean active) {
		this.changeStatusBarActive = active;
	}

	public boolean isLeftRulerActive() {
		return leftRulerActive;
	}

	public void setLeftRulerActive(boolean setLeftRulerActive) {
		this.leftRulerActive = setLeftRulerActive;
	}

	public boolean isRightRulerActive() {
		return rightRulerActive;
	}

	public void setRightRulerActive(boolean rightRulerActive) {
		this.rightRulerActive = rightRulerActive;
	}

	public void setDialogEnabled(boolean dialogEnabled) {
		this.dialogEnabled = dialogEnabled;
	}

	public boolean isDialogEnabled() {
		return dialogEnabled;
	}

}

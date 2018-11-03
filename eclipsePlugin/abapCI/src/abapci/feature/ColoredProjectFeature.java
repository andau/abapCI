package abapci.feature;

public class ColoredProjectFeature {

	private boolean changeStatusBarActive;
	private boolean leftRulerActive;
	private boolean rightRulerActive;
	private boolean dialogEnabled;
	private boolean titleIconActive;
	private boolean statusBarWidgetActive;
	private int titleIconWidth;
	private int titleIconHeight;

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

	public boolean isTitleIconActive() {
		// TODO Auto-generated method stub
		return titleIconActive;
	}

	public void setTitleIconActive(boolean titleIconActive) {
		this.titleIconActive = titleIconActive;
	}

	public boolean isStatusBarWidgetActive() {
		// TODO Auto-generated method stub
		return statusBarWidgetActive;
	}

	public void setStatusBarWidgetActive(boolean statusBarWidgetActive) {
		this.statusBarWidgetActive = statusBarWidgetActive;
	}

	public void setDialogEnabled(boolean dialogEnabled) {
		this.dialogEnabled = dialogEnabled;
	}

	public boolean isDialogEnabled() {
		return dialogEnabled;
	}


	public boolean isActive() {
		return isChangeStatusBarActive() || isRightRulerActive() || isLeftRulerActive() || isTitleIconActive();
	}
	public int getTitleIconWidth() {
		return titleIconWidth;
	}

	public void setTitleIconWidth(int width) {
		this.titleIconWidth = width; 	
	}

	public void setTitleIconHeight(int heigth) {
		this.titleIconHeight = heigth;
	}

	public int getTitleIconHeight() {
		return titleIconHeight;
	}


}

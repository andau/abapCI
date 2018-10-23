package abapci.coloredProject.general;

import org.eclipse.swt.graphics.Color;

public class DisplayColor {

	private Color statusBarColor;
	private Color annotationBarColor;

	public DisplayColor(Color statusBarColor, Color annotationBarColor) {
		this.statusBarColor = statusBarColor;
		this.annotationBarColor = annotationBarColor;
	}

	public Color getStatusBarColor() {
		return statusBarColor;
	}

	public Color getAnnotationBarColor() {
		return annotationBarColor;
	}

	public void setStatusBarColor(Color statusBarColor) {
		this.statusBarColor = statusBarColor;
	}

	public void setAnnotationBarColor(Color annotationBarColor) {
		this.annotationBarColor = annotationBarColor;
	}

}

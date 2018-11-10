package abapci.coloredProject.model.projectColor;

import org.eclipse.swt.graphics.Color;

public class DefaultEclipseProjectColor implements IProjectColor {

	public DefaultEclipseProjectColor() {

	}

	@Override
	public Color getColor() {
		return null;
	}

	@Override
	public boolean isSuppressed() {
		return true;
	}

	@Override
	public void setSuppressed() {
		// Default color is suppressed by default
	}
}

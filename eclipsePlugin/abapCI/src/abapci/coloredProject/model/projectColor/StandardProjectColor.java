package abapci.coloredProject.model.projectColor;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class StandardProjectColor implements IProjectColor {

	private RGB rgb;
	private boolean suppressed;

	public StandardProjectColor(RGB rgb) {
		this.rgb = rgb;
	}

	public StandardProjectColor(Color generalColor) {
		this.rgb = generalColor.getRGB(); 
	}

	public Color getColor() {
		return new Color(Display.getCurrent(), rgb.red, rgb.green, rgb.blue);
	}

	@Override
	public boolean isSuppressed() {

		return suppressed;
	}

	@Override
	public void setSuppressed() {
		suppressed = true;

	}

}
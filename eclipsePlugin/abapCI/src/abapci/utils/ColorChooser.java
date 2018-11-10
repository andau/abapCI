package abapci.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorChooser {

	RGB blackRGB = new RGB(0, 0, 0);
	RGB whiteRGB = new RGB(255, 255, 255);

	public Color getContrastColor(Color backgroundColor) {
		RGB contrastRGB = getLightness(backgroundColor) < 127 ? whiteRGB : blackRGB;
		return new Color(Display.getCurrent(), contrastRGB);
	}

	private float getLightness(Color backgroundColor) {
		return (minRgbValue(backgroundColor.getRGB()) + maxRgbValue(backgroundColor.getRGB())) / 2;
	}

	private int maxRgbValue(RGB rgb) {
		return Math.max(rgb.red, Math.max(rgb.green, rgb.blue));
	}

	private int minRgbValue(RGB rgb) {
		return Math.min(rgb.red, Math.min(rgb.green, rgb.blue));
	}

}

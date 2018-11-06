package abapci.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorChooser {

	RGB blackRGB = new RGB(0,0,0); 
	RGB whiteRGB = new RGB(255,255,255); 
	public Color getContrastColor(Color backgroundColor) {
		RGB contrastRGB = getBrightness(backgroundColor) < 0.5  ? whiteRGB : blackRGB;   
		return new Color(Display.getCurrent(), contrastRGB); 
	}
	private float getBrightness(Color backgroundColor) {
		return  backgroundColor.getRGB().getHSB()[2]; 
	}

}

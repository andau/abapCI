package abapci.utils;

import org.eclipse.swt.graphics.RGB;

import abapci.domain.UiColor;

public class ColorToRGBMapper {

	private ColorToRGBMapper() {
	}

	public static RGB mapUiColorToTheme(UiColor uiColor) {
		RGB rgb;

		switch (uiColor) {
		case STANDARD:
			rgb = null;
			break;
		case LIGHT_RED:
			rgb = new RGB(255, 153, 153);
			break;
		case RED:
			rgb = new RGB(255, 0, 0);
			break;
		case LIGHT_GREEN:
			rgb = new RGB(153, 255, 153);
			break;
		case GREEN:
			rgb = new RGB(0, 128, 0);
			break;
		case LIGHT_BLUE:
			rgb = new RGB(153, 153, 255);
			break;
		case BLUE:
			rgb = new RGB(0, 0, 255);
			break;
		case LIGHT_GRAY:
			rgb = new RGB(204, 204, 204);
			break;
		case GRAY:
			rgb = new RGB(128, 128, 128);
			break;
		case LIGHT_ORANGE:
			rgb = new RGB(255, 219, 153);
			break;
		case ORANGE:
			rgb = new RGB(255, 165, 0);
			break;
		case LIGHT_YELLOW:
			rgb = new RGB(255, 255, 153);
			break;
		case YELLOW:
			rgb = new RGB(255, 255, 0);
			break;
		default:
			rgb = null;
			break;
		}

		return rgb;
	}
}

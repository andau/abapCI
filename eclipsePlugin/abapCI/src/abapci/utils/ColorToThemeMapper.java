package abapci.utils;

import abapci.domain.UiColor;
import abapci.domain.UiTheme;

public class ColorToThemeMapper {

	private ColorToThemeMapper() {
	}

	public static UiTheme mapUiColorToTheme(UiColor uiColor) {
		UiTheme uiTheme = UiTheme.STANDARD_THEME;

		switch (uiColor) {
		case DEFAULT:
			uiTheme = UiTheme.STANDARD_THEME;
			break;
		case LIGHT_RED:
			uiTheme = UiTheme.LIGHT_RED_THEME;
			break;
		case RED:
			uiTheme = UiTheme.RED_THEME;
			break;
		case LIGHT_GREEN:
			uiTheme = UiTheme.LIGHT_GREEN_THEME;
			break;
		case GREEN:
			uiTheme = UiTheme.GREEN_THEME;
			break;
		case LIGHT_BLUE:
			uiTheme = UiTheme.LIGHT_BLUE_THEME;
			break;
		case BLUE:
			uiTheme = UiTheme.BLUE_THEME;
			break;
		case LIGHT_GRAY:
			uiTheme = UiTheme.LIGHT_GRAY_THEME;
			break;
		case GRAY:
			uiTheme = UiTheme.GRAY_THEME;
			break;
		case LIGHT_ORANGE:
			uiTheme = UiTheme.LIGHT_ORANGE_THEME;
			break;
		case ORANGE:
			uiTheme = UiTheme.ORANGE_THEME;
			break;
		case LIGHT_YELLOW:
			uiTheme = UiTheme.LIGHT_YELLOW_THEME;
			break;
		case YELLOW:
			uiTheme = UiTheme.YELLOW_THEME;
			break;
		}

		return uiTheme;
	}
}

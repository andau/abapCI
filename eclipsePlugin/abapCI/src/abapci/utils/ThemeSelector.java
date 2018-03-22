package abapci.utils;

import abapci.domain.UiTheme;

public class ThemeSelector {

	public UiTheme Select(String projectName) {
        //TODO eval Project to Color view preference settings  
		return UiTheme.STANDARD_THEME; 
	}
}
